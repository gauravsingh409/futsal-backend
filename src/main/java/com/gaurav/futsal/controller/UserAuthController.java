package com.gaurav.futsal.controller;

import com.gaurav.futsal.dto.request.UserRequestDto;
import com.gaurav.futsal.dto.response.UserDto;
import com.gaurav.futsal.entity.UserEntity;
import com.gaurav.futsal.payload.ApiResponse;
import com.gaurav.futsal.repository.UserRepository;
import com.gaurav.futsal.services.UserService;
import com.gaurav.futsal.validation.UserValidation;
import com.gaurav.futsal.validator.ValidRole;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class UserAuthController {

    private final UserRepository userRepository;
    private final UserService userService;


    @PostMapping("/public/auth/user/register")
    public ResponseEntity<?> registerUser(
            @Validated(UserValidation.UserRegisterGroup.class) @RequestBody UserEntity request) {
        UserDto savedUser = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.builder().code(200).message("User register successfully").data(savedUser));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(
            @Validated(UserValidation.UserLoginGroup.class) @RequestBody UserEntity userAuthDTO) {

        // Return Token
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/auth/owner/register")
    public ResponseEntity<?> registerFutsal(
            @Validated(UserValidation.OwnerRegisterGroup.class) @RequestBody UserRequestDto request) {


        return ResponseEntity.ok("ok");
    }

    @PostMapping("/auth/owner/login")
    public ResponseEntity<?> loginFootsal(
            @Validated(UserValidation.OwnerLoginGroup.class) @RequestBody UserRequestDto request) {

        return ResponseEntity.ok("ok");
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<?> tokenRefresh(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping(value = "/admin/user/get-all") // admin
    public ResponseEntity<?> getAllUser(
            HttpServletRequest httpServletRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int page_size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @ValidRole(allowed = {"user",
                    "admin"}, message = "Only admin or user roles are allowed") String role) {
        return ResponseEntity.ok("ok");
    }

    @PutMapping(value = "/complete-profile/owner", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> ownerCompleteProfile(
            @RequestPart("data") @Validated(UserValidation.UserCompleteProfileGroup.class) UserRequestDto request,
            @RequestPart("photos") MultipartFile photos, HttpServletRequest httpRequest) {


        return ResponseEntity.ok("ok");
    }

    @PutMapping(value = "/complete-profile/user", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> userCompleteProfile(
            @RequestPart("data") @Validated(UserValidation.UserCompleteProfileGroup.class) UserEntity request,
            @RequestPart("photos") MultipartFile photos, HttpServletRequest httpRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .body("ok");
    }

    // get the user details
    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserDetails(@PathVariable("id") Long userId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok("ok");
    }

    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok("ok");
    }

    @GetMapping(path = "/auth/me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok("ok");
    }
}