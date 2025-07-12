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

import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;

@RestController
@RequestMapping("/api/auth/owner")
public class OwnerAuthController {

   private final UserRepository userRepository;
   private final BCryptPasswordEncoder passwordEncoder;
   private final JwtService jwtService;

   // using constructor injection
   public OwnerAuthController(
         UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
         JwtService jwtService) {
      this.userRepository = userRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtService = jwtService;
   }

   @PostMapping("/register") // become /api/auth/futsal/register
   public ResponseEntity<?> registerFutsal(
         @Validated(UserValidation.OwnerRegisterGroup.class) @RequestBody UserRequestDto request) {
      // Check if the username exists
      if (userRepository.findByUsername(request.getUsername()) != null) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("Username already exists",
               409, false);
         return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
      }

      UserModel footsal = new UserModel();
      footsal.setIs_user(true);
      footsal.setIs_owner(true);
      footsal.setUsername(request.getUsername());
      footsal.setPassword(passwordEncoder.encode(request.getPassword()));
      footsal.setCitizenshipNumber(request.getCitizenshipNumber());
      footsal.setPhone_no(request.getPhone_no());

      UserModel savedUser = userRepository.save(footsal);

      Map<String, Object> data = new HashMap<>();
      data.put("id", savedUser.getId());
      data.put("username", savedUser.getUsername());
      ApiResponse<Map<String, Object>> response = new ApiResponse<>("User created successfully", 201, true, data);

      return ResponseEntity.ok(response);
   }

   @PostMapping("/login")
   public ResponseEntity<?> loginFootsal(
         @Validated(UserValidation.OwnerLoginGroup.class) @RequestBody UserRequestDto request) {

      Map<String, Object> data = new HashMap<>();
      Map<String, String> token = new HashMap<>();

      // Find the user in the database
      UserModel user = userRepository.findByUsername(request.getUsername());
      if (user == null) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("user doesn't exists", 400, false);
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
      }

      // Verify password
      if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("password incorrect", 400, false);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
      }

      // Generate Access & Refresh token
      String accessToken = jwtService
            .generateAccessToken(request.getUsername(),
                  user.getId());
      String refreshToken = jwtService
            .generateRefreshToken(request.getUsername(),
                  user.getId());

      token.put("access", accessToken);
      token.put("refresh", refreshToken);
      data.put("token", token);
      data.put("user", user);
      return ResponseEntity.ok(new ApiResponse<>("You are logged in", 200, true, data));
   }

}
