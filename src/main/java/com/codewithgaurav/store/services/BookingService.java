package com.codewithgaurav.store.services;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Service;
import com.codewithgaurav.store.dto.response.BookingResponseDto;
import com.codewithgaurav.store.entity.BookingEntity;
import com.codewithgaurav.store.mapper.BookingMapper;
import com.codewithgaurav.store.repository.BookingRepository;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final FutsalService futsalService;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, UserService userService, FutsalService futsalService,
            BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.futsalService = futsalService;
        this.bookingMapper = bookingMapper;
    }

    // Save the booking
    public BookingEntity saveBooking(BookingEntity request, Long futsalId, Long userId) {
        request.setUser(userService.getUserDetailsById(userId));
        request.setFutsal(futsalService.getFutsalById(futsalId));
        request.setOwner(futsalService.getUserByFutsalId(futsalId));
        return bookingRepository.save(request);
    }

    // convert toDto
    public BookingResponseDto converToDto(BookingEntity bookingEntity) {
        return bookingMapper.toDto(bookingEntity);
    }

    // is already booked
    public boolean isAlreadyBooked(Long futsalId, LocalDate bookedDate, LocalTime bookedTime) {
        System.out.println("Futsal id " + futsalId);
        System.out.println("Booked date " + bookedDate);
        return bookingRepository.existsByFutsal_IdAndBookedDateAndBookedTime(futsalId, bookedDate, bookedTime);
    }
}