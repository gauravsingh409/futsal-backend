package com.codewithgaurav.store.repository;

import com.codewithgaurav.store.entity.FutsalEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface FutsalRepository extends JpaRepository<FutsalEntity, Long> {
    Optional<FutsalEntity> findByRegistrationNumber(String futsalRegistrationNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    Page<FutsalEntity> findByUser_IdAndNameContainingIgnoreCase(Long ownerId, String keyword,
            @NonNull Pageable pageable);

    // Get all the futsals with pagination
    @NonNull
    Page<FutsalEntity> findAll(@NonNull Pageable pageable);

    Page<FutsalEntity> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query(value = """
            SELECT *
            FROM (
                SELECT f.*,
                    (6371 * acos(
                        cos(radians(:userLat)) * cos(radians(f.latitude)) *
                        cos(radians(f.longitude) - radians(:userLng)) +
                        sin(radians(:userLat)) * sin(radians(f.latitude))
                    )) AS distance
                FROM futsals f
            ) AS futsal_with_distance
            WHERE distance <= :radius
            ORDER BY distance
            """, countQuery = """
            SELECT COUNT(*)
            FROM (
                SELECT f.id,
                    (6371 * acos(
                        cos(radians(:userLat)) * cos(radians(f.latitude)) *
                        cos(radians(f.longitude) - radians(:userLng)) +
                        sin(radians(:userLat)) * sin(radians(f.latitude))
                    )) AS distance
                FROM futsals f
            ) AS futsal_with_distance
            WHERE distance <= :radius
            """, nativeQuery = true)
    Page<FutsalEntity> findFutsalsWithinRadius(
            @Param("userLat") Double userLat,
            @Param("userLng") Double userLng,
            @Param("radius") int radius,
            Pageable pageable);

    boolean existsByIdAndUser_Id(Long futsalId, Long userId);

}