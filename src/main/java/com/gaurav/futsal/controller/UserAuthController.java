package com.gaurav.futsal.controller;

import com.gaurav.futsal.dto.request.UserRegisterRequestDto;
import com.gaurav.futsal.dto.request.UserRequestDto;
import com.gaurav.futsal.dto.response.UserResponseDto;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
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


    @GetMapping("/user/exist")
    public ResponseEntity<?> isUserExist(
            @RequestParam() String email
    ) {
        UserResponseDto userResponseDto = userService.getUserDetails(email);
        return ResponseEntity.ok(new ApiResponse<UserResponseDto>("user details retrieved", 200, true, userResponseDto, null));
    }

    @PostMapping("/auth/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequestDto request) {
        UserResponseDto savedUser = userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse<UserResponseDto>("User saved successfully", 200, true, savedUser, null));
    }


    @PostMapping("/auth/login")
    public ResponseEntity<?> authenticateUser(
            @Validated(UserValidation.UserLoginGroup.class) @RequestBody UserEntity userAuthDTO) {
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

    @GetMapping("/auth/me")
    public ResponseEntity<?> getLoggedInUser() {
        // Get the authentication object
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        // extract claims from the JWT
        Map<String, Object> userInfo = Map.of(
                "keycloakId", jwt.getClaim("sub"),
                "email", jwt.getClaim("email"),
                "name", jwt.getClaim("name"),
                "emailVerified", jwt.getClaim("email_verified")
        );
        return ResponseEntity.ok(ApiResponse.builder().message("User details fetched").code(200).data(userInfo).success(true).build());
    }
}