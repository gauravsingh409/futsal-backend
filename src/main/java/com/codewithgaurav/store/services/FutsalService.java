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

    @Autowired
    private FutsalMapper futsalMapper;

    // IS Requested user is Owner
    public boolean isOwner(Long id) {
        // isPresent return true if the user found else false
        Optional<UserEntity> user = userRepository.findById(id);
        return user.get().isOwner();
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

    /**
     * Saves a Futsal entity along with its cover image, registration photo, and
     * multiple futsal images.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Sets the cover image URL on the futsal entity.</li>
     * <li>Sets the registration photo URL on the futsal entity.</li>
     * <li>Converts the list of futsal image URLs into FutsalImages entities, sets
     * their parent futsal,
     * and attaches them to the futsal entity.</li>
     * <li>Saves the futsal entity along with all associated images to the
     * database.</li>
     * </ol>
     *
     * @param request              The FutsalEntity object to save. Must be a
     *                             properly initialized entity.
     * @param coverImageUrl        The relative URL of the cover image (e.g.,
     *                             "/upload/cover/cover.jpg").
     * @param registrationImageUrl The relative URL of the registration photo (e.g.,
     *                             "/upload/registration/reg.jpg").
     * @param futsalImagesUrl      A list of relative URLs for additional futsal
     *                             images. Each URL will be converted
     *                             into a FutsalImages entity and linked to the
     *                             futsal entity.
     *
     *                             <p>
     *                             <strong>Example usage:</strong>
     *                             </p>
     * 
     *                             <pre>
     *                             List<String> images = List.of("/upload/futsal/1.jpg", "/upload/futsal/2.jpg");
     *                             futsalService.saveFutsal(futsalEntity, "/upload/cover/cover.jpg", "/upload/registration/reg.jpg", images);
     *                             </pre>
     *
     *                             <p>
     *                             <strong>Notes:</strong>
     *                             </p>
     *                             <ul>
     *                             <li>The FutsalEntity must have the images list
     *                             initialized or will be set in this method.</li>
     *                             <li>Ensure that the URLs provided are already
     *                             stored files accessible by the application.</li>
     *                             <li>If CascadeType.ALL is enabled on the images
     *                             relationship, saving the futsal entity will
     *                             automatically save the images.</li>
     *                             </ul>
     */
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
        return futsalMapper.toDto(request);
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
        return futsalMapper.toDto(updated);
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
    public Page<FutsalResponseDTO> getFilterFutsal(String search, Double latitude, Double longitude,
            Integer radius, Pageable pageable) {
        Page<FutsalEntity> futsalPage;
        if (latitude == null)
            futsalPage = futsalRepo.findByNameContainingIgnoreCase(search, pageable);
        else
            futsalPage = futsalRepo.findFutsalsWithinRadius(latitude, longitude, radius, pageable);
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

    // Is futsal exist to specific user or not
    public boolean existByUserIdAndFutsalId(Long futsalId, Long userId) {
        return futsalRepo.existsByIdAndUser_Id(futsalId, userId);
    }

    // get user by futsal id
    public UserEntity getUserByFutsalId(Long futsalId) {
        FutsalEntity futsalEntity = this.getFutsalById(futsalId);
        return futsalEntity.getUser();
    }

}
