package com.codewithgaurav.store.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.dto.response.BookingResponseDto;
import com.codewithgaurav.store.entity.BookingEntity;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.payload.PaginatedResponse;
import com.codewithgaurav.store.services.BookingService;
import com.codewithgaurav.store.services.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/")
public class BookingController {

    private final JwtService jwtService;
    private final BookingService bookingService;

    // Constructor Injection
    public BookingController(JwtService jwtService, BookingService bookingService) {
        this.jwtService = jwtService;
        this.bookingService = bookingService;
    }

    @PostMapping(path = { "booking", "/booking/create" }, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createBooking(
            @RequestBody BookingEntity request,
            @RequestParam(name = "futsal") Long futsalId,
            HttpServletRequest httpServletRequest) {
        jwtService.isValidAdminOrOwner(httpServletRequest);
        Long userId = jwtService.extractId(httpServletRequest);
        // Check the futsal is already booked or not
        boolean isAlreadyBooked = bookingService.isAlreadyBooked(futsalId, request.getBookedDate(),
                request.getBookedTime());
        if (isAlreadyBooked)
            return ResponseEntity.badRequest().body(new ApiResponse<>("Futsal already booked", 400, false));
        BookingEntity bookingEntity = bookingService.saveBooking(request, futsalId, userId);
        BookingResponseDto bookingResponseDto = bookingService.converToDto(bookingEntity);
        return ResponseEntity.ok().body(new ApiResponse<>("Booking success", 200, true, bookingResponseDto));
    }

    @GetMapping(path = { "/booking/user" })
    public ResponseEntity<?> getUserBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            HttpServletRequest httpServletRequest) {
        jwtService.isValidUser(httpServletRequest);
        Long id = jwtService.extractId(httpServletRequest);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BookingEntity> bookingPage = bookingService.getUserBooking(id, pageable);
        Page<BookingResponseDto> bookingPageDto = bookingService.convertPageToDto(bookingPage);
        var response = new PaginatedResponse<>(
                bookingPageDto.getContent(),
                bookingPageDto.getNumber(),
                bookingPageDto.getSize(),
                bookingPageDto.getTotalElements(),
                bookingPageDto.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Booking retrieve successfully", 200, true,
                response));
    }

    @GetMapping(path = { "/booking/owner" })
    public ResponseEntity<?> getOwnerBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            HttpServletRequest httpServletRequest) {
        jwtService.isValidAdminOrOwner(httpServletRequest);
        Long id = jwtService.extractId(httpServletRequest);
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BookingEntity> bookingPage = bookingService.getUserBooking(id, pageable);
        Page<BookingResponseDto> bookingPageDto = bookingService.convertPageToDto(bookingPage);
        var response = new PaginatedResponse<>(
                bookingPageDto.getContent(),
                bookingPageDto.getNumber(),
                bookingPageDto.getSize(),
                bookingPageDto.getTotalElements(),
                bookingPageDto.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse<>("Booking retrieve successfully", 200, true,
                response));
    }

}
