package com.codewithgaurav.store.services;

import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.model.FutsalModel;
import com.codewithgaurav.store.model.OwnerModel;
import com.codewithgaurav.store.repository.OwnerRepository;
import com.codewithgaurav.store.repository.futsal.FutsalRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    OwnerRepository repo;

    @Autowired
    FutsalRepository futsalRepo;

    // IS Requested user is Owner
    public boolean isOwner(String id) {
        // isPresent return true if the user found else false
        Optional<OwnerModel> owner = repo.findById(id);
        return owner.map(OwnerModel::isIs_owner).orElse(false);
    }

    // Store Cover Image when register
    public boolean storeCoverImage(MultipartFile cover_image, FutsalModel request) {
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
            request.setConverImage(imageUrl);
            return true;
        } catch (IOException e) {
            // throw new RuntimeException("Error while saving the cover image", e);
            throw new RuntimeException(e);
        }
    }

    // Store Multiple Image of Futsal
    public boolean storeMultipleImages(MultipartFile[] images, FutsalModel request) {
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
    public boolean storeRegistrationPhoto(MultipartFile file, FutsalModel request) {
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
    public boolean registerFutsalDetailsWithOwnerId(FutsalModel request, String id) {
        OwnerModel owner = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("user not found"));
        request.setOwner(owner);
        FutsalModel saved = futsalRepo.save(request);
        return saved != null && saved.getId() != null;
    }

    // check the futsal with registration number exists
    public boolean isFutsalAlreadyRegistered(String registrationNumber) {
        return futsalRepo.existsByRegistrationNumber(registrationNumber);
    }
}
