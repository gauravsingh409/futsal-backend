package com.gaurav.futsal.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gaurav.futsal.entity.BookingEntity;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {

    boolean existsByFutsal_IdAndBookedDateAndBookedTime(Long futsalId, LocalDate bookedDate, LocalTime bookedTime);

    Page<BookingEntity> findByUser_Id(Long id, Pageable pageable);

    Page<BookingEntity> findByOwner_Id(Long id, Pageable pageable);
}
