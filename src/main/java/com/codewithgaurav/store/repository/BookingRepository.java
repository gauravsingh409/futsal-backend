package com.codewithgaurav.store.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithgaurav.store.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    boolean existsByFutsal_IdAndBookedDateAndBookedTime(Long futsalId, LocalDate bookedDate, LocalTime bookedTime);
}
