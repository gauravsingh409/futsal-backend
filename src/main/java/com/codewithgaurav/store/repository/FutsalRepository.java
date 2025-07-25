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

   Page<FutsalEntity> findByUser_Id(Long ownerId, @NonNull Pageable pageable);

   // Get all the futsals with pagination
   @NonNull
   Page<FutsalEntity> findAll(@NonNull Pageable pageable);

   // @Query("""
   // SELECT f FROM Futsal f
   // WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
   // OR LOWER(f.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
   // OR LOWER(f.owner) LIKE LOWER(CONCAT('%', :keyword, '%'))
   // """)
   // Page<FutsalEntity> searchByKeyword(@Param("keyword") String keyword, Pageable
   // pageable);

}
