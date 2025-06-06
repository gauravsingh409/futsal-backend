package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import com.codewithgaurav.store.validation.UserValidation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.model.OwnerModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.repository.OwnerRepository;
import com.codewithgaurav.store.services.JwtService;

@RestController
@RequestMapping("/api/auth/owner")
public class OwnerAuthController {

   private final OwnerRepository footsalRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final JwtService jwtService;

   // using constructor injection
   public OwnerAuthController(OwnerRepository footsalRepository, BCryptPasswordEncoder passwordEncoder,
         JwtService jwtService) {
      this.footsalRepository = footsalRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtService = jwtService;
   }

   @PostMapping("/register") // become /api/auth/futsal/register
   public ResponseEntity<?> registerFutsal(
         @Validated(UserValidation.OwnerRegisterGroup.class) @RequestBody OwnerModel request) {
      // Check if the username exists
      if (footsalRepository.findByUsername(request.getUsername()) != null) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("Username already exists",
               409, false);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
      }

      OwnerModel footsal = new OwnerModel();
      footsal.setUsername(request.getUsername());
      footsal.setPassword(passwordEncoder.encode(request.getPassword()));
      footsal.setCitizenshipNumber(request.getCitizenshipNumber());
      footsal.setPhone_no(request.getPhone_no());
      OwnerModel savedUser = footsalRepository.save(footsal);

      // Generate JWT token
      String token = jwtService.generateToken(savedUser.getUsername(), savedUser.getId());
      Map<String, Object> data = new HashMap<>();
      data.put("id", savedUser.getId());
      data.put("username", savedUser.getUsername());
      data.put("token", token);
      ApiResponse<Map<String, Object>> response = new ApiResponse<>("User created successfully", 201, true, data);

      return ResponseEntity.ok(response);
   }

   @PostMapping("/login")
   public ResponseEntity<?> loginFootsal(
         @Validated(UserValidation.OwnerLoginGroup.class) @RequestBody OwnerModel request) {

      // Find the user in the database
      OwnerModel footsal = footsalRepository.findByUsername(request.getUsername());
      if (footsal == null) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("user doesn't exists", 400, false);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      // Verify password
      if (!passwordEncoder.matches(request.getPassword(), footsal.getPassword())) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("password incorrect", 401, false);
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }

      // Generate JWT token
      String token = jwtService.generateToken(footsal.getUsername(), footsal.getId());

      // Return token
      Map<String, Object> data = new HashMap<>();
      data.put("token", token);
      data.put("id", footsal.getId());
      data.put("username", footsal.getUsername());
      ApiResponse<Map<String, Object>> response = new ApiResponse<>("Logged in successfully", 200, true, data);
      return ResponseEntity.ok(response);
   }

}
