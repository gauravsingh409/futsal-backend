package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.dto.FootsalAuth;
import com.codewithgaurav.store.model.FootsalModel;
import com.codewithgaurav.store.repository.FootsalRepository;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.validation.FutsalLoginGroup;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/footsal")
public class FootsalAuthController {

   private final FootsalRepository footsalRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final JwtService jwtService;

   // using constructor injection
   public FootsalAuthController(FootsalRepository footsalRepository, BCryptPasswordEncoder passwordEncoder,
         JwtService jwtService) {
      this.footsalRepository = footsalRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtService = jwtService;
   }

   @PostMapping("/register") // become /api/auth/footsal/register
   public ResponseEntity<?> registerFootsal(@Valid @RequestBody FootsalAuth request) {
      Map<String, Object> response = new HashMap<>();
      Map<String, Object> data = new HashMap<>();

      // Check if the username exists
      if (footsalRepository.findByUsername(request.getUsername()) != null) {
         response.put("message", "Username already exists");
         return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
      }

      FootsalModel footsal = new FootsalModel();
      footsal.setUsername(request.getUsername());
      footsal.setPassword(passwordEncoder.encode(request.getPassword()));
      footsal.setContact_info(request.getContact_info());
      footsal.setFootsal_name(request.getFootsal_name());
      footsal.setRegistration_number(request.getRegistration_number());
      footsal.setImage(request.getImage());
      footsal.setPrice_per_hour(request.getPrice_per_hour());
      FootsalModel savedUser = footsalRepository.save(footsal);

      // Generate JWT token
      String token = jwtService.generateToken(savedUser.getUsername());

      data.put("username", savedUser.getUsername());
      data.put("token", token);
      response.put("message", "Footsal registered successfully");
      response.put("data", data);
      response.put("status", 200);

      return ResponseEntity.ok(response);
   }

   @PostMapping("/login")
   public ResponseEntity<?> loginFootsal(@Validated(FutsalLoginGroup.class) @RequestBody FootsalAuth request) {
      Map<String, Object> response = new HashMap<>();
      Map<String, Object> data = new HashMap<>();

      // Find the user in the database
      FootsalModel footsal = footsalRepository.findByUsername(request.getUsername());
      if (footsal == null) {
         response.put("message", "Cannot find the username");
         response.put("status", 404);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      // Verify password
      if (!passwordEncoder.matches(request.getPassword(), footsal.getPassword())) {
         response.put("message", "Invalid password");
         response.put("status", 401);
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }

      // Generate JWT token
      String token = jwtService.generateToken(request.getUsername());

      // Return token
      data.put("token", token);
      data.put("username", request.getUsername());
      response.put("message", "Login successful");
      response.put("status", 200);
      response.put("data", data);
      return ResponseEntity.ok(response);
   }

}
