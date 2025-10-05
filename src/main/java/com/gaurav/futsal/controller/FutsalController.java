package com.gaurav.futsal.controller;

import com.gaurav.futsal.dto.response.FutsalResponseDTO;
import com.gaurav.futsal.entity.FutsalEntity;
import com.gaurav.futsal.entity.FutsalImages;
import com.gaurav.futsal.payload.ApiResponse;
import com.gaurav.futsal.payload.PaginatedResponse;
import com.gaurav.futsal.services.FutsalService;
import com.gaurav.futsal.validation.FutsalValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FutsalController {

    private final FutsalService futsalService;


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerFutsal(
            @RequestPart("registration_photo") MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalEntity request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart("cover_image") MultipartFile coverImage,
            HttpServletRequest httpServletRequest) {
        Long id = 1L;
        futsalService.isFutsalAlreadyRegistered(request.getRegistrationNumber());
        String registrationImageUrl = futsalService.storeFile(registraionPhoto, "upload/registration");
        String coverImageUrl = futsalService.storeFile(coverImage, "upload/cover");
        List<String> imagesUrl = futsalService.storeFiles(images, "upload/images");
        FutsalResponseDTO futsalResponseDTO = futsalService.saveFutsal(request, id, coverImageUrl, registrationImageUrl,
                imagesUrl);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal Registered successfully", 201, true, futsalResponseDTO,null));
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateFutsal(
            @PathVariable("id") Long futsalId,
            @RequestPart(value = "registration_photo", required = false) MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalEntity request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "cover_image", required = false) MultipartFile coverImage,
            HttpServletRequest httpServletRequest) {
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
                new ApiResponse<>("Futsal updated successfully", 200, true, futsalResponseDTO,null));
    }

    // get the futsal
    @GetMapping(value = "/get-all")
    public ResponseEntity<?> getFutsals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            @RequestParam(value = "latitude", required = false) Double latitude,
            @RequestParam(value = "longitude", required = false) Double longitude,
            @RequestParam(value = "radius", required = false) Integer radius) {
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page or page size is not valid", 400, false,null,null));
        }
        if (pageSize > 30) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page size exceed the limit", 400, false,null,null));
        }
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<FutsalResponseDTO> futsalPage = futsalService.getFilterFutsal(search, latitude, longitude, radius,
                pageable);
        var response = new PaginatedResponse(
                futsalPage.getContent(),
                futsalPage.getNumber(),
                futsalPage.getSize(),
                futsalPage.getTotalElements(),
                futsalPage.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Futsal retrieved successfully", 200, true, response,null));
    }

    // get logged owner futsal
    @GetMapping(value = "/owner/get")
    public ResponseEntity<?> getOwnerFutsals(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false, defaultValue = "") String search,
            HttpServletRequest httpServletRequest) {
        Long id = 1L;
        // Validate the page and pageSize
        if (page < 0 || pageSize <= 0) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page or page size is not valid", 400, false,null,null));
        }
        if (pageSize > 30) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("page size exceed the limit", 400, false,null,null));
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
                .body(new ApiResponse<>("Futsal retrieved successfully", 200, true, response,null));
    }

    // get futsal details
    @GetMapping(value = "/get/{id}")
    public ResponseEntity<?> getFutsalById(
            @PathVariable("id") Long futsalId,
            HttpServletRequest httpServletRequest) {
        boolean isValidUser = true;

        if (isValidUser) {
            FutsalResponseDTO futsal = futsalService.getFutsalDetails(futsalId);
            return ResponseEntity.ok().body(new ApiResponse<>("Futsal details retrieved", 200, true, futsal,null));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Permission Not Allowed", 401, false,null,null));
        }
    }

    // delete the futsal
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteFutsalById(
            @PathVariable("id") Long futsalId,
            HttpServletRequest httpServletRequest) {
        boolean isDeleted = futsalService.deleteFutsalById(futsalId);
        if (!isDeleted)
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(String.format("futsal %s not found", futsalId), 400, false,null,null));
        return ResponseEntity.ok().body(new ApiResponse<>());
    }

    // // Get the futsal based on the location
    // @GetMapping(value = "/get/specific")
    // public ResponseEntity<?> getLocationBasedFutsal(HttpServletRequest
    // httpServletRequest,
    // @RequestParam("latitude") double latitude,
    // @RequestParam("longitude") double longitude,
    // @RequestParam("radius") int radius) {
    // jwtService.extractValidUserId(httpServletRequest);
    // List<FutsalResponseDTO> futsals = futsalService.getNearByFutsals(latitude,
    // longitude, radius);
    // return ResponseEntity.ok()
    // .body(new ApiResponse<>("Futsal retrived successfully", 200, true, futsals));
    // }
}
