package com.codewithgaurav.store.services;

import com.codewithgaurav.store.dto.response.FutsalDetailsDto;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.model.FutsalModel;
import com.codewithgaurav.store.model.OwnerModel;
import com.codewithgaurav.store.repository.OwnerRepository;
import com.codewithgaurav.store.repository.futsal.FutsalRepository;

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

    // update futsal data (JSON) to database
    public boolean updateFutsalDetailsByFutsalId(FutsalModel futsal, String futsalId) {
        System.out.println(futsal.toString());
        FutsalModel existingFutsal = futsalRepo.findById(futsalId).get();
        existingFutsal.setName(futsal.getName());
        existingFutsal.setDistrict(futsal.getDistrict());
        existingFutsal.setCity(futsal.getCity());
        existingFutsal.setLocation(futsal.getLocation());
        if (futsal.getImages() != null && !futsal.getImages().isEmpty()) {
            existingFutsal.setImages(futsal.getImages());
        }
        if (futsal.getConverImage() != null && !futsal.getConverImage().isEmpty()) {
            existingFutsal.setConverImage(futsal.getConverImage());
        }
        FutsalModel saved = futsalRepo.save(existingFutsal);
        return saved != null && saved.getId() != null;
    }

    // check the futsal with registration number exists
    public boolean isFutsalAlreadyRegistered(String registrationNumber) {
        return futsalRepo.existsByRegistrationNumber(registrationNumber);
    }

    // Get All Futsal
    public Page<FutsalModel> getAllFutsals(Pageable pageable) {
        return futsalRepo.findAll(pageable);
    }

    // Futsal search with Pagination
    public Page<FutsalModel> searchFutsals(String search, Pageable pageable) {
        Page<FutsalModel> futsalPage;

        if (search == null || search.trim().isEmpty())
            futsalPage = futsalRepo.findAll(pageable); // fallback to all
        else
            futsalPage = futsalRepo.searchByKeyword(search, pageable);

        return futsalPage.map(futsal -> {
            futsal.setConverImage("http://localhost:8080" + futsal.getConverImage());
            return futsal;
        });
    }

    // owner Futsals
    public Page<FutsalModel> getOwnerFutsals(String id, Pageable pageable) {
        Page<FutsalModel> futsalPage;

        futsalPage = futsalRepo.findByOwner_Id(id, pageable);

        return futsalPage.map(futsal -> {
            futsal.setConverImage("http://localhost:8080" + futsal.getConverImage());
            return futsal;
        });

    }

    // get futsal by id
    public FutsalModel getFutsalDetails(String id) {
        FutsalModel futsal = futsalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Futsal not found with ID: " + id));
        String baseurl = "http://localhost:8080";
        List<String> updatedImages = futsal.getImages().stream().map(image -> baseurl + image).toList();
        String coverImage = baseurl + futsal.getConverImage();
        String registrationPhoto = baseurl + futsal.getRegistrationPhoto();

        futsal.setRegistrationPhoto(registrationPhoto);
        futsal.setConverImage(coverImage);
        futsal.setImages(updatedImages);
        return futsal;
    }

    // convert to FutsalDTO
    public FutsalDetailsDto convertToFutsalDto(FutsalModel futsal) {
        ModelMapper modelMapper = new ModelMapper();
        FutsalDetailsDto data = modelMapper.map(futsal, FutsalDetailsDto.class);

        // data.setId(futsal.getId());
        // data.setName(futsal.getName());
        // data.setOwner;
        return data;
    }

    // delete futsal images by futsal id
    public boolean deleteFutsalImagesByFutsalId(String futsalId) {
        FutsalModel futsal = futsalRepo.findById(futsalId).get();
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
    public boolean deleteCoverImageByFutsalId(String futsalId) {
        FutsalModel futsal = futsalRepo.findById(futsalId).get();
        if (futsal.getConverImage() != null && !futsal.getConverImage().isEmpty()) {
            File file = new File(futsal.getConverImage());
            if (file.exists())
                file.delete();
        }
        return true;
    }

}
