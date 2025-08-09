package com.codewithgaurav.store.repository;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithgaurav.store.entity.TimeSlotEntity;

public interface TimeSlotRepository extends JpaRepository<TimeSlotEntity, Long> {
   List<TimeSlotEntity> findByFutsalId(Long futsalId);

   boolean existsByFutsalIdAndStartTime(Long FutsalId, LocalTime starTime);
}
