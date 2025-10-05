package com.gaurav.futsal.services;

import com.gaurav.futsal.dto.response.FutsalResponseDTO;
import com.gaurav.futsal.entity.FutsalEntity;
import com.gaurav.futsal.entity.FutsalImages;
import com.gaurav.futsal.entity.UserEntity;
import com.gaurav.futsal.exception.ResourceNotFoundException;
import com.gaurav.futsal.repository.FutsalRepository;
import com.gaurav.futsal.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FutsalService {

    @Value("${app.base-url}")
    private String baseUrl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FutsalRepository futsalRepo;


    // IS Requested user is Owner
    public boolean isOwner(Long id) {
        // isPresent return true if the user found else false
        Optional<UserEntity> user = userRepository.findById(id);
        return true;
    }

    public FutsalEntity getFutsalById(Long futsaId) {
        return futsalRepo.findById(futsaId)
                .orElseThrow(() -> new ResourceNotFoundException("Futsal with id " + futsaId + " not found!"));
    }

    public String storeFile(MultipartFile file, String path) {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            throw new RuntimeException("File name is invalid");
        }
        String sanitizedFilename = originalFileName
                .replaceAll("\\s+", "_") // replace spaces with underscores
                .replaceAll("[^a-zA-Z0-9._-]", "");
        try {
            Path basePath = Paths.get(System.getProperty("user.dir"));
            Path uploadPath = basePath.resolve(path);
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);
            String fileName = System.currentTimeMillis() + "_" + sanitizedFilename;
            Path targetPath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            return "/" + path + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + e.getMessage(), e);
        }
    }

    public void deleteFile(String relativePath) {
        if (relativePath == null || relativePath.isEmpty())
            return;
        try {
            Path filePath = Paths.get(System.getProperty("user.dir") + relativePath).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }
        } catch (Exception e) {
            System.out.println("Failed to delete file");
        }
    }

    public List<String> storeFiles(MultipartFile[] files, String path) {
        List<String> fileUrls = new ArrayList<>();
        Path basePath = Paths.get(System.getProperty("user.dir"));
        Path uploadPath = basePath.resolve(path).normalize();
        try {
            if (!Files.exists(uploadPath))
                Files.createDirectories(uploadPath);

            for (MultipartFile file : files) {
                if (file.isEmpty())
                    continue;

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path targePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), targePath, StandardCopyOption.REPLACE_EXISTING);
                fileUrls.add("/" + path + "/" + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store files: " + e.getMessage(), e);
        }
        return fileUrls;
    }

    public FutsalResponseDTO saveFutsal(FutsalEntity request, Long userId, String coverImageUrl,
            String registrationImageUrl,
            List<String> futsalImagesUrl) {
        UserEntity user = userRepository.findById(userId).get();
        if (coverImageUrl != null)
            request.setCoverImage(coverImageUrl);
        if (registrationImageUrl != null)
            request.setRegistrationPhoto(registrationImageUrl);
        if (futsalImagesUrl != null && !futsalImagesUrl.isEmpty()) {
            List<FutsalImages> futsalImages = futsalImagesUrl.stream().map(url -> {
                FutsalImages images = new FutsalImages();
                images.setImageUrl(url);
                images.setFutsal(request);
                return images;
            }).collect(Collectors.toList());
            request.setImages(futsalImages);
        }
        request.setUser(user);
        futsalRepo.save(request);
        return new FutsalResponseDTO();
    }

    public void saveImagesUrl(FutsalEntity request, List<String> images) {
        if (images != null && !images.isEmpty()) {
            for (String img : images) {

                FutsalImages futsalImages = new FutsalImages();
                futsalImages.setImageUrl(img);
                futsalImages.setFutsal(request);
                request.getImages().add(futsalImages);
            }
        }
    }

    public FutsalResponseDTO updateFutsal(FutsalEntity request, FutsalEntity existing) {
        existing.setName(request.getName());
        existing.setDistrict(request.getDistrict());
        existing.setLatitude(request.getLatitude());
        existing.setLongitude(request.getLongitude());
        existing.setCity(request.getCity());
        FutsalEntity updated = futsalRepo.save(existing);
        return new FutsalResponseDTO();
    }

    // Register futsal data (JSON) to database
    public boolean registerFutsalDetailsWithOwnerId(FutsalEntity request, Long id) {
        UserEntity owner = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found"));
        request.setUser(owner);
        FutsalEntity saved = futsalRepo.save(request);
        return saved.getId() != null;
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
    public Page<FutsalResponseDTO> getFilterFutsal(String search, Double latitude, Double longitude,
            Integer radius, Pageable pageable) {
        Page<FutsalEntity> futsalPage;
        if (latitude == null)
            futsalPage = futsalRepo.findByNameContainingIgnoreCase(search, pageable);
        else
            futsalPage = futsalRepo.findFutsalsWithinRadius(latitude, longitude, radius, pageable);
        return futsalPage.map(this::getFutsalDto);
    }

    // owner Futsals
    public Page<FutsalResponseDTO> getOwnerFutsals(Long id, String search, Pageable pageable) {
        Page<FutsalEntity> futsalPage;
        futsalPage = futsalRepo.findByUser_IdAndNameContainingIgnoreCase(id, search, pageable);
        return futsalPage.map(this::getFutsalDto);

    }



    // get futsal by id
    public FutsalResponseDTO getFutsalDetails(Long id) {
        FutsalEntity futsal = futsalRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Futsal not found with ID: " + id));
        return new FutsalResponseDTO();
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
        return new FutsalResponseDTO();
    }

    // Is futsal exist to specific user or not
    public boolean existByUserIdAndFutsalId(Long futsalId, Long userId) {
        return futsalRepo.existsByIdAndUser_Id(futsalId, userId);
    }

    // get user by futsal id
    public UserEntity getUserByFutsalId(Long futsalId) {
        FutsalEntity futsalEntity = this.getFutsalById(futsalId);
        return futsalEntity.getUser();
    }


    private FutsalResponseDTO getFutsalDto(FutsalEntity futsal) {
        return new FutsalResponseDTO();
    }

}
