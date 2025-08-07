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
import java.util.Map;
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
            HttpServletRequest httpRequest) {
        Long id = jwtService.extractValidOwnerId(httpRequest);

        if (id != null) {
            // Check the futsal with this registration number is registered or not
            boolean isFutsalAlreadyRegistered = futsalService
                    .isFutsalAlreadyRegistered(request.getRegistrationNumber());
            if (isFutsalAlreadyRegistered)
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>("Futsal Already Registered", 409, false));
            // Store the Registration Photo
            boolean isRegistrationPhotoStored = futsalService.storeRegistrationPhoto(registraionPhoto, request);
            if (!isRegistrationPhotoStored)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("Some error occurred while saving the registration photo", 400, false));
            // Store Cover Images
            boolean isCoverImageStored = futsalService.storeCoverImage(coverImage, request);
            if (!isCoverImageStored)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("Some error occurred while saving the cover image", 400, false));

            // Store the Multiple Images
            boolean isImagesStored = futsalService.storeMultipleImages(images, request);
            if (!isImagesStored)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("Some error occurred while saving the images", 400, false));

            // Store the Futsal Data JSON
            boolean isDataStored = futsalService.registerFutsalDetailsWithOwnerId(request, id);
            if (!isDataStored)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("some Error occured while saving the data", 400, false));
            FutsalResponseDTO dto = new FutsalResponseDTO();
            dto.setId(request.getId());
            dto.setName(request.getName());
            dto.setCity(request.getCity());
            dto.setDistrict(request.getDistrict());
            dto.setRegistrationNumber(request.getRegistrationNumber());
            dto.setRegistrationPhoto(request.getRegistrationPhoto());
            dto.setCoverImage(request.getCoverImage());
            dto.setLatitude(request.getLatitude());
            dto.setLongitude(request.getLongitude());
            List<String> imageUrls = request.getImages().stream()
                    .map(FutsalImages::getImageUrl)
                    .toList();
            dto.setImages(imageUrls);

            // Data successfully store to databse
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ApiResponse<>("Futsal Registered successfully", 200, true, dto));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>("You don't have permission to perform this action", 403, false));
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

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<FutsalResponseDTO> futsalPage = futsalService.getFilterFutsal(search, pageable);
        System.out.println(futsalPage.toString());

        // Java will automatically infer the type of response
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
            @RequestParam(required = false) String search,
            HttpServletRequest httpServletRequest) {

        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        Long id = jwtService.extractId(token);
        boolean isOwner = futsalService.isOwner(id);

        if (!isOwner)
            return ResponseEntity.badRequest().body(new ApiResponse<>("You don't have permission", 400, false, null));

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
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        Long id = jwtService.extractId(token);

        if (id != null && !id.toString().trim().isEmpty()) {
            FutsalResponseDTO futsal = futsalService.getFutsalDetails(futsalId);
            return ResponseEntity.ok().body(new ApiResponse<>("Futsal details retrieved", 200, true, futsal));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Permission Not Allowed", 401, false));
        }
    }

    // update the futsal
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<?> updateFutsalById(
            @RequestPart(value = "registration_photo", required = false) MultipartFile registraionPhoto,
            @RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalEntity request,
            @RequestPart(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "cover_image", required = false) MultipartFile coverImage,
            @PathVariable("id") Long futsalId,
            HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        Long id = jwtService.extractId(token);
        boolean isOwner = futsalService.isOwner(id);
        if (isOwner) {
            // Delete Images if user has uploaded new images
            if (images != null && images.length > 0) {
                futsalService.deleteFutsalImagesByFutsalId(futsalId);
            }
            if (coverImage != null && !coverImage.isEmpty()) {
                futsalService.deleteCoverImageByFutsalId(futsalId);
            }
            // Store Cover Images
            if (coverImage != null && !coverImage.isEmpty()) {
                boolean isCoverImageStored = futsalService.storeCoverImage(coverImage, request);
                if (!isCoverImageStored) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>("Some error occurred while saving the cover image", 400, false));
                }
            }

            // Store the Multiple Images
            if (images != null && images.length > 0) {
                boolean isImagesStored = futsalService.storeMultipleImages(images, request);
                if (!isImagesStored)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new ApiResponse<>("Some error occurred while saving the images", 400, false));
            }

            FutsalEntity futsal = futsalService.updateFutsalById(request, futsalId);
            FutsalResponseDTO futsalDto = futsalService.convertToFutsalDto(futsal);
            if (futsal != null)
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ApiResponse<>("Futsal Updated successfully", 200, true, futsalDto));
            else
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiResponse<>("some Error occured while saving the data", 400, false));
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

        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        Long id = jwtService.extractId(token);
        boolean isOwner = futsalService.isOwner(id);

        if (!isOwner)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>("Permission Not Allowed", 401, false));
        Boolean isDeleted = futsalService.deleteFutsalById(futsalId);
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
