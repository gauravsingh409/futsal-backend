package com.gaurav.futsal.controller;

import com.gaurav.futsal.dto.response.BookingResponseDto;
import com.gaurav.futsal.entity.BookingEntity;
import com.gaurav.futsal.payload.ApiResponse;
import com.gaurav.futsal.payload.PaginatedResponse;
import com.gaurav.futsal.services.BookingService;
import lombok.RequiredArgsConstructor;
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
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping(path = {"booking",
            "/booking/create"}, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createBooking(
            @RequestBody BookingEntity request,
            @RequestParam(name = "futsal") Long futsalId,
            HttpServletRequest httpServletRequest) {
        Long userId = 1L;
        // Check the futsal is already booked or not
        boolean isAlreadyBooked = bookingService.isAlreadyBooked(futsalId, request.getBookedDate(),
                request.getBookedTime());
        if (isAlreadyBooked)
            return ResponseEntity.badRequest().body(new ApiResponse<>("Futsal already booked", 400, false, null, null));
        BookingEntity bookingEntity = bookingService.saveBooking(request, futsalId, userId);
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        return ResponseEntity.ok().body(new ApiResponse<>("Booking success", 200, true, bookingResponseDto, null));
    }

    @GetMapping(path = {"/booking/user"})
    public ResponseEntity<?> getUserBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            HttpServletRequest httpServletRequest) {
        Long id = 1L;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BookingEntity> bookingPage = bookingService.getUserBooking(id, pageable);
        Page<BookingResponseDto> bookingPageDto = bookingService.convertPageToDto(bookingPage);
        var response = new PaginatedResponse(
                bookingPageDto.getContent(),
                bookingPageDto.getNumber(),
                bookingPageDto.getSize(),
                bookingPageDto.getTotalElements(),
                bookingPageDto.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Booking retrieve successfully", 200, true,
                        response, null));
    }

    @GetMapping(path = {"/booking/owner"})
    public ResponseEntity<?> getOwnerBooking(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String search,
            HttpServletRequest httpServletRequest) {
        Long id = 1L;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<BookingEntity> bookingPage = bookingService.getUserBooking(id, pageable);
        Page<BookingResponseDto> bookingPageDto = bookingService.convertPageToDto(bookingPage);
        var response = new PaginatedResponse<>(
                bookingPageDto.getContent(),
                bookingPageDto.getNumber(),
                bookingPageDto.getSize(),
                bookingPageDto.getTotalElements(),
                bookingPageDto.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiResponse<>("Booking retrieve successfully", 200, true,
                        response, null));
    }

}
