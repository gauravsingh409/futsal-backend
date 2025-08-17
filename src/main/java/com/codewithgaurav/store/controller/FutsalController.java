package com.codewithgaurav.store.controller;

import com.codewithgaurav.store.dto.response.FutsalResponseDTO;
import com.codewithgaurav.store.entity.FutsalEntity;
import com.codewithgaurav.store.entity.FutsalImages;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/futsal")
public class FutsalController {

    @Autowired
    FutsalService futsalService;

    @Autowired
    JwtService jwtService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerFutsal(
            @RequestPart("registration_photo") MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalEntity request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart("cover_image") MultipartFile coverImage,
            HttpServletRequest httpServletRequest) {
        jwtService.isValidAdminOrOwner(httpServletRequest);
        Long id = jwtService.extractId(httpServletRequest);
        futsalService.isFutsalAlreadyRegistered(request.getRegistrationNumber());
        String registrationImageUrl = futsalService.storeFile(registraionPhoto, "upload/registration");
        String coverImageUrl = futsalService.storeFile(coverImage, "upload/cover");
        List<String> imagesUrl = futsalService.storeFiles(images, "upload/images");
        FutsalResponseDTO futsalResponseDTO = futsalService.saveFutsal(request, id, coverImageUrl, registrationImageUrl,
                imagesUrl);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal Registered successfully", 201, true, futsalResponseDTO));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateFutsal(
            @PathVariable("id") Long futsalId,
            @RequestPart(value = "registration_photo", required = false) MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalEntity request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "cover_image", required = false) MultipartFile coverImage,
            HttpServletRequest httpServletRequest) {
        jwtService.isValidAdminOrOwner(httpServletRequest);
        FutsalEntity existingData = futsalService.getFutsalById(futsalId);
        if (registraionPhoto != null && !registraionPhoto.isEmpty()) {
            futsalService.deleteFile(existingData.getRegistrationPhoto());
            String registrationImageUrl = futsalService.storeFile(registraionPhoto, "upload/registration");
            existingData.setRegistrationPhoto(registrationImageUrl);
        }
        if (coverImage != null && !coverImage.isEmpty()) {
            futsalService.deleteFile(existingData.getCoverImage());
            String coverImageUrl = futsalService.storeFile(coverImage, "upload/cover");
            existingData.setCoverImage(coverImageUrl);
        }
        if (images != null && images.length > 0 && existingData.getImages() != null) {
            for (FutsalImages futsalImages : existingData.getImages())
                futsalService.deleteFile(futsalImages.getImageUrl());
            existingData.getImages().clear();
            List<String> imagesUrl = futsalService.storeFiles(images, "upload/images");
            futsalService.saveImagesUrl(existingData, imagesUrl);
        }
        FutsalResponseDTO futsalResponseDTO = futsalService.updateFutsal(request, existingData);
        return ResponseEntity.ok().body(
                new ApiResponse<>("Futsal updated successfully", 200, true, futsalResponseDTO));
    }

    // get the futsal
    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getFutsals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search) {
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page or page size is not valid", 400, false));
        }
        if (pageSize > 30) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page size exceed the limit", 400, false));
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<FutsalResponseDTO> futsalPage = futsalService.getFilterFutsal(search, pageable);
        var response = new PaginatedResponse<>(
                futsalPage.getContent(),
                futsalPage.getNumber(),
                futsalPage.getSize(),
                futsalPage.getTotalElements(),
                futsalPage.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal retrieved successfully", 200, true, response));
    }

    // get logged owner futsal
    @GetMapping(value = "/owner/get")
    public ResponseEntity<?> getOwnerFutsals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "") String search,
            HttpServletRequest httpServletRequest) {
        Long id = jwtService.extractValidOwnerId(httpServletRequest);
        // Validate the page and pageSize
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page or page size is not valid", 400, false));
        }
        if (pageSize > 30) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page size exceed the limit", 400, false));
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<FutsalResponseDTO> futsalPage = futsalService.getOwnerFutsals(id, search, pageable);

        var response = new PaginatedResponse<>( // java will automatically infer the type of response
                // PaginatedResponse<FutsalEntity> paginatedData = new PaginatedResponse<>(
                futsalPage.getContent(),
                futsalPage.getNumber(),
                futsalPage.getSize(),
                futsalPage.getTotalElements(),
                futsalPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal retrieved successfully", 200, true, response));
    }

    // get futsal details
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getFutsalById(
            @PathVariable("id") Long futsalId,
            HttpServletRequest httpServletRequest) {
        boolean isValidUser = jwtService.isValidUser(httpServletRequest);

        if (isValidUser) {
            FutsalResponseDTO futsal = futsalService.getFutsalDetails(futsalId);
            return ResponseEntity.ok().body(new ApiResponse<>("Futsal details retrieved", 200, true, futsal));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Permission Not Allowed", 401, false));
        }
    }

    // delete the futsal
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteFutsalById(
            @PathVariable("id") Long futsalId,
            HttpServletRequest httpServletRequest) {
        jwtService.extractValidOwnerId(httpServletRequest);
        boolean isDeleted = futsalService.deleteFutsalById(futsalId);
        if (!isDeleted)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(String.format("futsal %s not found", futsalId), 400, false));
        return ResponseEntity.ok().body(new ApiResponse<>());
    }

    // Get the futsal based on the location
    @GetMapping(value = "/get/specific")
    public ResponseEntity<?> getLocationBasedFutsal(HttpServletRequest httpServletRequest,
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") int radius) {
        jwtService.extractValidUserId(httpServletRequest);
        List<FutsalResponseDTO> futsals = futsalService.getNearByFutsals(latitude, longitude, radius);
        return ResponseEntity.ok()
                .body(new ApiResponse<>("Futsal retrived successfully", 200, true, futsals));
    }
}
