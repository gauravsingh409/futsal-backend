package com.codewithgaurav.store.controller.Futsal;

import com.codewithgaurav.store.model.FutsalModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.payload.PaginatedResponse;
import com.codewithgaurav.store.services.FutsalService;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.validation.FutsalValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/futsal")
public class FutsalController {

    @Autowired
    FutsalService service;

    @Autowired
    JwtService jwtService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerFutsal(
            @RequestPart("registration_photo") MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalModel request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart("cover_image") MultipartFile coverImage,
            HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        String id = jwtService.extractId(token);
        boolean isOwner = service.isOwner(id);

        // Check the futsal with this registration number is registered or not
        boolean isFutsalAlreadyRegistered = service.isFutsalAlreadyRegistered(request.getRegistrationNumber());
        if (isFutsalAlreadyRegistered)
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>("Futsal Already Registered", 409, false));

        // Store the Futsal Details in the database
        if (isOwner) {
            // Store the Registration Photo
            boolean isRegistrationPhoto = service.storeRegistrationPhoto(registraionPhoto, request);
            if (!isRegistrationPhoto)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("Registration Photo not saved", 400, false));

            // Store Cover Images
            if (coverImage != null && !coverImage.isEmpty()) {
                boolean isCoverImageStored = service.storeCoverImage(coverImage, request);
                if (!isCoverImageStored) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>("Some error occurred while saving the cover image", 400, false));
                }
            }

            // Store the Multiple Images
            if (images != null && images.length != 0) {
                boolean isImagesStored = service.storeMultipleImages(images, request);
                if (!isImagesStored)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>("some Error occured while saving the file", 400, false));
            }

            // Store the Futsal Data JSON
            boolean isDataStore = service.registerFutsalDetailsWithOwnerId(request, id);
            if (!isDataStore)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("some Error occured while saving the file", 400, false));
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>("Futsal Registered successfully", 200, true, request));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("user not authorized", 401, false));
        }
    }

    // get the futsal
    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getFutsals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search) {

        // Validate the page and pageSize
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page or page size is not valid", 400, false));
        }
        if (pageSize > 30) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page size exceed the limit", 400, false));
        }

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("name").ascending());
        Page<FutsalModel> futsalPage = service.searchFutsals(search, pageable);

        var response = new PaginatedResponse<>( // java will automatically infer the type of response
                // PaginatedResponse<FutsalModel> paginatedData = new PaginatedResponse<>(
                futsalPage.getContent(),
                futsalPage.getNumber(),
                futsalPage.getSize(),
                futsalPage.getTotalElements(),
                futsalPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal retrieved successfully", 200, true, response));
    }
}
