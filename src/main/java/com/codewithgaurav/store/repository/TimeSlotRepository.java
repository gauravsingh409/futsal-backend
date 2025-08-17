package com.codewithgaurav.store.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.codewithgaurav.store.entity.TimeSlotEntity;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {
   List<TimeSlotEntity> findByFutsalId(Long futsalId);

   boolean existsByFutsal_IdAndStartTime(Long FutsalId, LocalTime starTime);

   @Query("""
         SELECT t
         FROM TimeSlotEntity t
         WHERE CAST(t.price AS string) LIKE %:keyword%
         AND t.futsal.id = :futsalId
         """)
   Page<TimeSlotEntity> findByPriceAndFutsalId(
         @Param("keyword") String keyword,
         @Param("futsalId") Long futsalId,
         Pageable pageable);
}
