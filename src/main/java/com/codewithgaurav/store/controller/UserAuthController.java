package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import com.codewithgaurav.store.validation.UserValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;

@RestController
@RequestMapping("/api/auth/user") // Base path for all methods in this controller
public class UserAuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtservice;

    public UserAuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtservice = jwtService;
    }

    @PostMapping("/register") // Becomes /api/auth/register
    public ResponseEntity<?> registerUser(
            @Validated(UserValidation.UserRegisterGroup.class) @RequestBody UserModel request) {
        Map<String, Object> data = new HashMap<>();

        // Validate input
        if (request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Invalid Username and password", 400, false));
        }

        // Check if username exists
        if (userRepository.findByUsername(request.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>("Username already exists", 409, false));
        }

        // Hash password and save user
        UserModel newUserModel = new UserModel();
        newUserModel.setUsername(request.getUsername());
        newUserModel.setPassword(passwordEncoder.encode(request.getPassword()));
        UserModel savedUserModel = userRepository.save(newUserModel);

        data.put("token", savedUserModel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("User Created Successfully", 201, false, savedUserModel));
    }

    @PostMapping("/login") // Becomes /api/auth/register
    public ResponseEntity<?> authenticateUser(
            @Validated(UserValidation.UserLoginGroup.class) @RequestBody UserModel userAuthDTO) {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> token = new HashMap<>();

        // Find user in database
        UserModel existingUserModel = userRepository.findByUsername(userAuthDTO.getUsername());
        if (existingUserModel == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("User not found", 400, false));
        }

        // Verify password
        if (!passwordEncoder.matches(userAuthDTO.getPassword(), existingUserModel.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("Password Wrong", 400, false));
        }

        // Generate JWT token
        String accessToken = jwtservice.generateAccessToken(existingUserModel.getUsername(), existingUserModel.getId());
        String refreshToken = jwtservice.generateRefreshToken(existingUserModel.getUsername(),
                existingUserModel.getId());
        token.put("access", accessToken);
        token.put("refresh", refreshToken);
        data.put("token", token);
        data.put("user", existingUserModel);
        // Return Token
        return ResponseEntity.ok(new ApiResponse<>("Login Successful", 200, true, data));
    }

}