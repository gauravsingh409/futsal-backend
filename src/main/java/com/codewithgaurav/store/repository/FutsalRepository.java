package com.codewithgaurav.store.repository;

import com.codewithgaurav.store.entity.FutsalEntity;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface FutsalRepository extends JpaRepository<FutsalEntity, Long> {
   Optional<FutsalEntity> findByRegistrationNumber(String futsalRegistrationNumber);

   boolean existsByRegistrationNumber(String registrationNumber); // check does this exists in database or not

   Page<FutsalEntity> findByUser_IdAndNameContainingIgnoreCase(Long ownerId, String keyword,
         @NonNull Pageable pageable);

   // Get all the futsals with pagination
   @NonNull
   Page<FutsalEntity> findAll(@NonNull Pageable pageable);

   Page<FutsalEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
