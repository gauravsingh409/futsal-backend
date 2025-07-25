package com.codewithgaurav.store.services;

import com.codewithgaurav.store.dto.response.FutsalDetailsDto;
import com.codewithgaurav.store.entity.FutsalEntity;
import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.repository.FutsalRepository;
import com.codewithgaurav.store.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    FutsalRepository futsalRepo;

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
        List<String> storedImages = new ArrayList<>();
        Path uploadImagesPath = Paths.get(System.getProperty("user.dir"), "uploads", "futsal", "images");
        try {
            Files.createDirectories(uploadImagesPath);
            for (MultipartFile image : images) {
                String originalName = image.getOriginalFilename();
                if (originalName == null || !originalName.contains(".")) {
                    continue;
                }
                String fileExtension = originalName.substring(originalName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;

                File destination = new File(uploadImagesPath.toFile(), uniqueFileName);
                image.transferTo(destination);
                String imageUrl = "/uploads/futsal/images/" + uniqueFileName;
                storedImages.add(imageUrl);
            }
            request.setImages(storedImages);
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
        // request.setOwner(owner);
        FutsalEntity saved = futsalRepo.save(request);
        return saved != null && saved.getId() != null;
    }

    // update futsal data (JSON) to database
    public boolean updateFutsalDetailsByFutsalId(FutsalEntity futsal, Long futsalId) {
        System.out.println(futsal.toString());
        FutsalEntity existingFutsal = futsalRepo.findById(futsalId).get();
        existingFutsal.setName(futsal.getName());
        existingFutsal.setDistrict(futsal.getDistrict());
        existingFutsal.setCity(futsal.getCity());
        existingFutsal.setLatitude(futsal.getLatitude());
        existingFutsal.setLongitude(futsal.getLongitude());
        if (futsal.getImages() != null && !futsal.getImages().isEmpty()) {
            existingFutsal.setImages(futsal.getImages());
        }
        if (futsal.getCoverImage() != null && !futsal.getCoverImage().isEmpty()) {
            existingFutsal.setCoverImage(futsal.getCoverImage());
        }
        FutsalEntity saved = futsalRepo.save(existingFutsal);
        return saved != null && saved.getId() != null;
    }

    // check the futsal with registration number exists
    public boolean isFutsalAlreadyRegistered(String registrationNumber) {
        return futsalRepo.existsByRegistrationNumber(registrationNumber);
    }

    // Get All Futsal
    public Page<FutsalEntity> getAllFutsals(Pageable pageable) {
        return futsalRepo.findAll(pageable);
    }

    // Futsal search with Pagination
    public Page<FutsalEntity> searchFutsals(String search, Pageable pageable) {
        Page<FutsalEntity> futsalPage;

        if (search == null || search.trim().isEmpty())
            futsalPage = futsalRepo.findAll(pageable); // fallback to all
        else
            // futsalPage = futsalRepo.searchByKeyword(search, pageable);
            futsalPage = futsalRepo.findAll(pageable); // fallback to all

        return futsalPage.map(futsal -> {
            futsal.setCoverImage("http://localhost:8080" + futsal.getCoverImage());
            return futsal;
        });
    }

    // owner Futsals
    public Page<FutsalEntity> getOwnerFutsals(Long id, Pageable pageable) {
        Page<FutsalEntity> futsalPage;

        futsalPage = futsalRepo.findByUser_Id(id, pageable);

        return futsalPage.map(futsal -> {
            futsal.setCoverImage("http://localhost:8080" + futsal.getCoverImage());
            return futsal;
        });

    }

    // get futsal by id
    public FutsalEntity getFutsalDetails(Long id) {
        FutsalEntity futsal = futsalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Futsal not found with ID: " + id));
        String baseurl = "http://localhost:8080";
        List<String> updatedImages = futsal.getImages().stream().map(image -> baseurl + image).toList();
        String coverImage = baseurl + futsal.getCoverImage();
        String registrationPhoto = baseurl + futsal.getRegistrationPhoto();

        futsal.setRegistrationPhoto(registrationPhoto);
        futsal.setCoverImage(coverImage);
        futsal.setImages(updatedImages);
        return futsal;
    }

    // convert to FutsalDTO
    public FutsalDetailsDto convertToFutsalDto(FutsalEntity futsal) {
        ModelMapper modelMapper = new ModelMapper();
        FutsalDetailsDto data = modelMapper.map(futsal, FutsalDetailsDto.class);

        // data.setId(futsal.getId());
        // data.setName(futsal.getName());
        // data.setOwner;
        return data;
    }

    // delete futsal images by futsal id
    public boolean deleteFutsalImagesByFutsalId(Long futsalId) {
        FutsalEntity futsal = futsalRepo.findById(futsalId).get();
        List<String> images = futsal.getImages();
        for (String image : images) {
            File file = new File(image);
            if (file.exists()) {
                file.delete();
            }
        }
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

}
