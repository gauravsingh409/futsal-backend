package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.codewithgaurav.store.validation.UserValidation;

import io.swagger.v3.oas.annotations.Operation;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;
// import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/") // Base path for all methods in this controller
@Tag(name = "User", description = "Endpoints for users")
public class UserAuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserAuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Operation(summary = "Register User", description = "Register User")
    @PostMapping("/auth/register") // Becomes /api/auth/user/register
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

    @PostMapping("/auth/login") // Becomes /api/auth/register
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
        String accessToken = jwtService.generateAccessToken(existingUserModel.getUsername(), existingUserModel.getId());
        String refreshToken = jwtService.generateRefreshToken(existingUserModel.getUsername(),
                existingUserModel.getId());
        token.put("access", accessToken);
        token.put("refresh", refreshToken);
        data.put("token", token);
        data.put("user", existingUserModel);
        // Return Token
        return ResponseEntity.ok(new ApiResponse<>("Login Successful", 200, true, data));
    }

    @PostMapping("/auth/owner/register") // become /api/auth/futsal/register
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

    @PostMapping("/auth/owner/login")
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

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> tokenRefresh(@RequestBody Map<String, String> body) {
        Map<String, String> token = new HashMap<>();

        String refresh = body.get("refresh");
        String id = jwtService.extractId(refresh);
        if (id != null && !id.isEmpty()) {
            String username = jwtService.extractUsername(refresh);
            String accessToken = jwtService.generateAccessToken(username, id);
            String refreshToken = jwtService.generateRefreshToken(username, id);
            token.put("access", accessToken);
            token.put("refresh", refreshToken);
            return ResponseEntity.ok().body(token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>("User not authenticated", 401, false));
    }

    @GetMapping(value = "/user/get-all")
    public ResponseEntity<?> getAllUsers(HttpServletRequest httpServletRequest) {
        String token = jwtService.extractToken(httpServletRequest);
        if (token == null || token.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>("Authentical Creadential not provided", 401, false));
        String userId = jwtService.extractId(token);
        Boolean isAdmin = jwtService.isAdmin(userId);
        if (!isAdmin)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new ApiResponse<>("Permission not allowed", 401, false));
        List<UserModel> users = userRepository.findAll();
        return ResponseEntity.ok().body(new ApiResponse<>("User retrieved successfully", 200, true, users));
    }

}