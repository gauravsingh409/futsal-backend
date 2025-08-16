package com.codewithgaurav.store.services;

import com.codewithgaurav.store.dto.response.FutsalResponseDTO;
import com.codewithgaurav.store.entity.FutsalEntity;
import com.codewithgaurav.store.entity.FutsalImages;
import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.mapper.FutsalMapper;
import com.codewithgaurav.store.repository.FutsalRepository;
import com.codewithgaurav.store.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FutsalService {

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FutsalRepository futsalRepo;

    @Autowired
    private FutsalMapper futsalMapper;

    // IS Requested user is Owner
    public boolean isOwner(Long id) {
        // isPresent return true if the user found else false
        Optional<UserEntity> user = userRepository.findById(id);
        return user.get().isOwner();
    }

    // Store Cover Image when register
    public boolean storeCoverImage(MultipartFile cover_image, FutsalEntity request) {
        String originalFilename = cover_image.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String coverImageName = UUID.randomUUID() + fileExtension;
        Path uploadCoverImagePath = Paths.get(System.getProperty("user.dir"), "uploads", "futsal", "cover");
        try {
            Files.createDirectories(uploadCoverImagePath);
        } catch (IOException e) {
            throw new RuntimeException("could not create upload directory", e);
        }
        File destination = new File(uploadCoverImagePath.toFile(), coverImageName);
        try {
            cover_image.transferTo(destination);
            String imageUrl = "/uploads/futsal/cover/" + coverImageName;
            request.setCoverImage(imageUrl);
            return true;
        } catch (IOException e) {
            // throw new RuntimeException("Error while saving the cover image", e);
            throw new RuntimeException(e);
        }
    }

    // Store Multiple Image of Futsal
    public boolean storeMultipleImages(MultipartFile[] images, FutsalEntity request) {
        List<FutsalImages> imageList = new ArrayList<>();
        Path uploadImagesPath = Paths.get(System.getProperty("user.dir"), "uploads", "futsal", "images");
        try {
            Files.createDirectories(uploadImagesPath);
            for (MultipartFile image : images) {
                String originalName = image.getOriginalFilename();
                if (originalName == null || !originalName.contains(".")) {
                    continue;
                }
                FutsalImages futsalImage = new FutsalImages();
                String fileExtension = originalName.substring(originalName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;

                File destination = new File(uploadImagesPath.toFile(), uniqueFileName);
                image.transferTo(destination);
                String imageUrl = "/uploads/futsal/images/" + uniqueFileName;
                futsalImage.setImageUrl(imageUrl);
                futsalImage.setFutsal(request);
                imageList.add(futsalImage);
            }
            request.setImages(imageList);
            return true;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store the images");
        }
    }

    // Store Registration Photo
    public boolean storeRegistrationPhoto(MultipartFile file, FutsalEntity request) {
        Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "futsal", "registration");
        String fileExtension = "";
        try {
            Files.createDirectories(uploadPath);
            String originalName = file.getOriginalFilename();
            if (originalName != null && originalName.contains(".")) {
                fileExtension = originalName.substring(originalName.lastIndexOf("."));
            }
            String fileName = UUID.randomUUID() + originalName + fileExtension;
            File destination = new File(uploadPath.toFile(), fileName);
            file.transferTo(destination);
            String fileUrl = "/uploads/futsal/registration/" + fileName;
            request.setRegistrationPhoto(fileUrl);
            return true;
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    // Register futsal data (JSON) to database
    public boolean registerFutsalDetailsWithOwnerId(FutsalEntity request, Long id) {
        UserEntity owner = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        request.setUser(owner);
        FutsalEntity saved = futsalRepo.save(request);
        return saved != null && saved.getId() != null;
    }

    // update futsal data (JSON) to database
    public FutsalEntity updateFutsalById(FutsalEntity futsal, Long futsalId) {
        FutsalEntity existingFutsal = futsalRepo.findById(futsalId).get();
        existingFutsal.setName(futsal.getName());
        existingFutsal.setDistrict(futsal.getDistrict());
        existingFutsal.setCity(futsal.getCity());
        existingFutsal.setLatitude(futsal.getLatitude());
        existingFutsal.setLongitude(futsal.getLongitude());
        if (futsal.getCoverImage() != null && !futsal.getCoverImage().isEmpty()) {
            existingFutsal.setCoverImage(futsal.getCoverImage());
        }
        FutsalEntity saved = futsalRepo.save(existingFutsal);
        return saved;
    }

    // check the futsal with registration number exists
    public boolean isFutsalAlreadyRegistered(String registrationNumber) {
        if (futsalRepo.existsByRegistrationNumber(registrationNumber))
            throw new RuntimeException("Futsal already registered");
        else
            return false;
    }

    // Get All Futsal
    public Page<FutsalEntity> getAllFutsals(Pageable pageable) {
        return futsalRepo.findAll(pageable);
    }

    // Get Futsal List
    public Page<FutsalResponseDTO> getFilterFutsal(String search, Pageable pageable) {
        Page<FutsalEntity> futsalPage;
        futsalPage = futsalRepo.findByNameContainingIgnoreCase(search, pageable);
        return futsalPage.map(futsalMapper::toDto);
    }

    // owner Futsals
    public Page<FutsalResponseDTO> getOwnerFutsals(Long id, String search, Pageable pageable) {
        Page<FutsalEntity> futsalPage;
        futsalPage = futsalRepo.findByUser_IdAndNameContainingIgnoreCase(id, search, pageable);
        return futsalPage.map(futsalMapper::toDto);

    }

    // get futsal by id
    public FutsalResponseDTO getFutsalDetails(Long id) {
        FutsalEntity futsal = futsalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Futsal not found with ID: " + id));
        return futsalMapper.toDto(futsal);
    }

    // delete futsal images by futsal id
    public boolean deleteFutsalImagesByFutsalId(Long futsalId) {
        // FutsalEntity futsal = futsalRepo.findById(futsalId).get();
        // List<String> images = futsal.getImages();
        // for (String image : images) {
        // File file = new File(image);
        // if (file.exists()) {
        // file.delete();
        // }
        // }
        return true;
    }

    // delete futsal cover image by futsal id
    public boolean deleteCoverImageByFutsalId(Long futsalId) {
        FutsalEntity futsal = futsalRepo.findById(futsalId).get();
        if (futsal.getCoverImage() != null && !futsal.getCoverImage().isEmpty()) {
            File file = new File(futsal.getCoverImage());
            if (file.exists())
                file.delete();
        }
        return true;
    }

    // delete the futsal
    public boolean deleteFutsalById(Long futsalId) {

        if (futsalRepo.existsById(futsalId)) {
            futsalRepo.deleteById(futsalId);
            return true;
        } else
            return false;
    }

    public FutsalResponseDTO convertToFutsalDto(FutsalEntity futsal) {
        return futsalMapper.toDto(futsal);
    }

    // Get the futsal based on longitude and latitude
    public List<FutsalResponseDTO> getNearByFutsals(Double latitude, Double longitude, int radius) {
        List<FutsalEntity> futsals = futsalRepo.findFutsalsWithinRadius(latitude, longitude, radius);
        return futsals.stream().map(this::convertToFutsalDto).toList();
    }

}
