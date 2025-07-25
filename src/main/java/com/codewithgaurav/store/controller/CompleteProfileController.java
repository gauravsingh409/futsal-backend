package com.codewithgaurav.store.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.validation.UserValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@RestController
@RequestMapping("/api/complete-profile")
public class CompleteProfileController {
    private static final Logger logger = LoggerFactory.getLogger(CompleteProfileController.class);

    @Autowired
    UserService service;

    @Autowired
    JwtService jwtService;

    @Autowired
    ObjectMapper objectMapper;

    @PutMapping(value = "/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
            UserEntity updatedOwner = service.updateOwnerDetails(id, request);

            return ResponseEntity.ok().body(new ApiResponse<>("Profile update successfully", 200, true, updatedOwner));

        } catch (io.jsonwebtoken.ExpiredJwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Token expired", 401, false));

        } catch (io.jsonwebtoken.JwtException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("Invalid token", 401, false));
        }
    }

    @PutMapping(value = "/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

        UserEntity updateUser = service.updateUserProfile(id, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Profile updates successfully", 200, true, updateUser));
    }
}
