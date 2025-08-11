package com.codewithgaurav.store.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.codewithgaurav.store.validation.UserValidation;
import com.codewithgaurav.store.validator.ValidRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.payload.PaginatedResponse;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.services.UserService;

@RestController
@RequestMapping("/api/") // Base path for all methods in this controller
@Tag(name = "User", description = "Endpoints for users")
@Validated
public class UserAuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserService userService;

    public UserAuthController(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Operation(summary = "Register User", description = "Register User")
    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(
            @Validated(UserValidation.UserRegisterGroup.class) @RequestBody UserEntity request) {
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
        UserEntity newUserModel = new UserEntity();
        newUserModel.setUsername(request.getUsername());
        newUserModel.setPassword(passwordEncoder.encode(request.getPassword()));
        UserEntity savedUserModel = userRepository.save(newUserModel);

        data.put("token", savedUserModel);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("User Created Successfully", 201, false, savedUserModel));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(
            @Validated(UserValidation.UserLoginGroup.class) @RequestBody UserEntity userAuthDTO) {
        Map<String, Object> data = new HashMap<>();
        Map<String, String> token = new HashMap<>();

        // Find user in database
        UserEntity existingUserModel = userRepository.findByUsername(userAuthDTO.getUsername());
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

    @PostMapping("/auth/owner/register")
    public ResponseEntity<?> registerFutsal(
            @Validated(UserValidation.OwnerRegisterGroup.class) @RequestBody UserRequestDto request) {
        // Check if the username exists
        if (userRepository.findByUsername(request.getUsername()) != null) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("Username already exists",
                    409, false);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        UserEntity footsal = new UserEntity();
        footsal.setUser(true);
        footsal.setOwner(true);
        footsal.setUsername(request.getUsername());
        footsal.setPassword(passwordEncoder.encode(request.getPassword()));
        footsal.setCitizenshipNumber(request.getCitizenshipNumber());

        UserEntity savedUser = userRepository.save(footsal);

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
        UserEntity user = userRepository.findByUsername(request.getUsername());
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
        Long id = jwtService.extractId(refresh);
        if (id != null) {
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

    @GetMapping(value = "/admin/user/get-all") // admin
    public ResponseEntity<?> getAllUser(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @ValidRole(allowed = { "user",
                    "admin" }, message = "Only admin or user roles are allowed") String role) {
        jwtService.extractValidAdminId(httpServletRequest);
        Pageable pageable = PageRequest.of(page, page_size);
        Page<UserEntity> users = userRepository.findAll(pageable);
        return ResponseEntity.ok().body(new ApiResponse<>("User retrieved successfully", 200, true,
                new PaginatedResponse<>(users.getContent(), users.getNumber(), users.getSize(),
                        users.getTotalElements(), users.getTotalPages())));
    }

    @PutMapping(value = "/complete-profile/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ownerCompleteProfile(
            @RequestPart("data") @Validated(UserValidation.UserCompleteProfileGroup.class) UserRequestDto request,
            @RequestPart("photos") MultipartFile photos, HttpServletRequest httpRequest) {

        try {
            String authHeader = httpRequest.getHeader("Authorization");

            // Check if token is present
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                ApiResponse<Map<String, Object>> response = new ApiResponse<>("User not authenticated", 401, false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            String token = authHeader.substring(7);
            Long id = jwtService.extractId(token);

            // ✅ Save photo
            String originalFilename = photos.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String fileName = UUID.randomUUID() + fileExtension;
            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "owners");

            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }

            File destination = new File(uploadPath.toFile(), fileName);
            try {
                photos.transferTo(destination);
            } catch (IOException e) {
                System.out.println("error while saving the file");
                throw new RuntimeException(e.getMessage());
            }

            // Set the profile image path before saving to DB
            String imageUrl = "/uploads/owners/" + fileName; // This should be a relative path
            request.setProfileImageUrl(imageUrl);

            // Save updated data to DB
            UserEntity updatedOwner = userService.updateOwnerDetails(id, request);

            return ResponseEntity.ok().body(new ApiResponse<>("Profile update successfully", 200, true, updatedOwner));

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Token expired", 401, false));

        } catch (io.jsonwebtoken.JwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Invalid token", 401, false));
        }
    }

    @PutMapping(value = "/complete-profile/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> userCompleteProfile(
            @RequestPart("data") @Validated(UserValidation.UserCompleteProfileGroup.class) UserEntity request,
            @RequestPart("photos") MultipartFile photos, HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        // check if token is present
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("Token not found", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        Long id = jwtService.extractId(token);
        String originalFilename = photos.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID() + fileExtension;
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "users");

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>(e.getMessage(), 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        File destination = new File(uploadPath.toFile(), fileName);
        try {
            photos.transferTo(destination);
        } catch (IOException e) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>(e.getMessage(), 400, false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        request.setProfilePicture("/uploads/users/" + fileName);

        UserEntity updateUser = userService.updateUserProfile(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Profile updates successfully", 200, true, updateUser));
    }

    // get the user details
    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable("id") Long userId, HttpServletRequest httpServletRequest) {
        jwtService.isValidAdminOrOwner(httpServletRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(new ApiResponse<>("User details retrieved successfully", 200, true,
                        userService.findDetailsById(userId)));
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId, HttpServletRequest httpServletRequest) {
        jwtService.extractValidAdminId(httpServletRequest);
        if (userService.deleteUser(userId))
            return ResponseEntity.ok().body(new ApiResponse<>("User deleted successfully", 200, true));
        else
            return ResponseEntity.ok().body(new ApiResponse<>("User with id " + userId + " not found", 400, false));
    }

}