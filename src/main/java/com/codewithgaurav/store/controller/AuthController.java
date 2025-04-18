package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.codewithgaurav.store.dto.AuthRequest;
import com.codewithgaurav.store.model.User;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.validation.LoginGroup;

@RestController
@RequestMapping("/api/auth") // Base path for all methods in this controller
public class AuthController {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final JwtService jwtservice;

   // Using constructor injection instead of field injection
   @Autowired
   public AuthController(UserRepository userRepository,
         BCryptPasswordEncoder passwordEncoder, JwtService jwtService) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtservice = jwtService;
   }

   @PostMapping("/register") // Becomes /api/auth/register
   public ResponseEntity<?> registerUser(@Validated(LoginGroup.class) @RequestBody AuthRequest request) {

      // Validate input
      if (request.getUsername() == null || request.getPassword() == null) {
         return ResponseEntity.badRequest().body("Username and password are required");
      }

      // Check if username exists
      if (userRepository.findByUsername(request.getUsername()) != null) {
         return ResponseEntity.badRequest().body("Username already taken");
      }

      // Hash password and save user
      User newUser = new User();
      newUser.setUsername(request.getUsername());
      newUser.setPassword(passwordEncoder.encode(request.getPassword()));
      User savedUser = userRepository.save(newUser);

      return ResponseEntity.ok("User registered successfully with ID: " + savedUser.getId());
   }

   @PostMapping("/login") // Becomes /api/auth/register
   public ResponseEntity<?> authenticateUser(
         @Validated(LoginGroup.class) @RequestBody AuthRequest authRequest) {

      // Find user in database
      User existingUser = userRepository.findByUsername(authRequest.getUsername());
      if (existingUser == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password!");
      }

      // Verify password
      if (!passwordEncoder.matches(authRequest.getPassword(), existingUser.getPassword())) {
         return ResponseEntity.badRequest().body("Invalid password");
      }

      // Generate JWT token
      String token = jwtservice.generateToken(authRequest.getUsername());

      // Return Token
      Map<String, Object> response = new HashMap<>();
      Map<String, Object> data = new HashMap<>();
      data.put("username", authRequest.getUsername());
      data.put("token", token);
      data.put("expiresIn", 3600);
      response.put("message", "Login Successful");
      response.put("data", data);
      return ResponseEntity.ok(response);
   }

}