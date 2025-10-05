package com.gaurav.futsal.services;

import java.time.LocalDate;
import java.time.LocalTime;

import com.gaurav.futsal.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gaurav.futsal.dto.response.BookingResponseDto;
import com.gaurav.futsal.entity.BookingEntity;
import com.gaurav.futsal.repository.BookingRepository;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final FutsalService futsalService;


    // Save the booking
    public BookingEntity saveBooking(BookingEntity request, Long futsalId, Long userId) {
        request.setUser(new UserEntity());
        request.setFutsal(futsalService.getFutsalById(futsalId));
        request.setOwner(futsalService.getUserByFutsalId(futsalId));
        return bookingRepository.save(request);
    }

    // User Booking
    public Page<BookingEntity> getUserBooking(Long id, Pageable pageable) {
        return bookingRepository.findByUser_Id(id, pageable);
    }

    // Owner Booking
    public Page<BookingEntity> getOwnerBooking(Long id, Pageable pageable) {
        return bookingRepository.findByOwner_Id(id, pageable);
    }

    // convert page to dto page
    public Page<BookingResponseDto> convertPageToDto(Page<BookingEntity> page) {
        return page.map(this::getBookingDto);
    }

    // is already booked
    public boolean isAlreadyBooked(Long futsalId, LocalDate bookedDate, LocalTime bookedTime) {
        return bookingRepository.existsByFutsal_IdAndBookedDateAndBookedTime(futsalId, bookedDate, bookedTime);
    }

    private BookingResponseDto getBookingDto(BookingEntity bookingEntity){
        return new BookingResponseDto();
    }
}