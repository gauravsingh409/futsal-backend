package com.codewithgaurav.store.controller.Futsal;

import com.codewithgaurav.store.model.FutsalModel;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.repository.futsal.FutsalRepository;
import com.codewithgaurav.store.services.FutsalService;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.validation.FutsalValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/futsal")
public class FutsalController {

    @Autowired
    FutsalService service;

    @Autowired
    JwtService jwtService;

    @Autowired
    FutsalRepository repo;


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerFutsal(@RequestPart("data") @Validated(FutsalValidation.FutsalRegister.class) FutsalModel request,
                                            @RequestPart("images") MultipartFile[] images,
                                            @RequestPart("cover_image") MultipartFile cover_image, HttpServletRequest httpRequest)
    {
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        String token = authHeader.substring(7);
        String id = jwtService.extractId(token);
        boolean isOwner = service.isOwner(id);

        if (isOwner) {
            boolean isCoverImageStored = service.storeCoverImage(cover_image, request);
            if (!isCoverImageStored) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>("some Error occured while saving the file", 400, false));
            boolean isImagesStored = service.storeMultipleImages(images, request);
            System.out.println("multiple image are store" + request.getFutsal_images());

            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Futsal Registered successfully", 200, false));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>("user not authorized", 401, false));
        }
    }
}
