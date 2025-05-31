package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import com.codewithgaurav.store.validation.UserValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;

@RestController
@RequestMapping("/api/auth") // Base path for all methods in this controller
public class UserAuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtservice;

    // Using constructor injection instead of field injection
    @Autowired
    public UserAuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtService;
    }

    @PostMapping("/register") // Becomes /api/auth/register
    public ResponseEntity<?> registerUser(@Validated(UserValidation.UserLoginGroup.class) @RequestBody UserModel request) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        // Validate input
        if (request.getUsername() == null || request.getPassword() == null) {
            response.put("code", 400);
            response.put("success", false);
            response.put("message", "Invalid username and password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Check if username exists
        if (userRepository.findByUsername(request.getUsername()) != null) {
            response.put("code", 409);
            response.put("message", "Username already taken");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Hash password and save user
        UserModel newUserModel = new UserModel();
        newUserModel.setUsername(request.getUsername());
        newUserModel.setPassword(passwordEncoder.encode(request.getPassword()));
        UserModel savedUserModel = userRepository.save(newUserModel);

        response.put("code", 201);
        response.put("message", "User Created Successfully");
        response.put("success", true);
        data.put("id", savedUserModel.getId());
        data.put("username", savedUserModel.getUsername());
        response.put("data", data);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login") // Becomes /api/auth/register
    public ResponseEntity<?> authenticateUser(@Validated(UserValidation.UserLoginGroup.class) @RequestBody UserModel userAuthDTO) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> error = new HashMap<>();

        // Find user in database
        UserModel existingUserModel = userRepository.findByUsername(userAuthDTO.getUsername());
        if (existingUserModel == null) {
            response.put("message", "Invalid username or password");
            response.put("code", 401);
            response.put("status", "failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Verify password
        if (!passwordEncoder.matches(userAuthDTO.getPassword(), existingUserModel.getPassword())) {
            error.put("message", "Invalid username or password");
            error.put("code", 401);
            response.put("status", "failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Generate JWT token
        String token = jwtservice.generateToken(existingUserModel.getUsername(), existingUserModel.getId());

        // Return Token

        data.put("username", userAuthDTO.getUsername());
        data.put("token", token);
        data.put("expiresIn", 3600);
        response.put("success", true);
        response.put("message", "Login Successful");
        response.put("data", data);
        response.put("code", 200);
        return ResponseEntity.ok(response);
    }

}