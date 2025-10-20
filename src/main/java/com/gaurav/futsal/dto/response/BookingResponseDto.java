package com.gaurav.futsal.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BookingResponseDto {
    private Long id;
    private String customerName;
    private String phone;
    private LocalDate bookedDate;
    private String status;
    private LocalDateTime createAt;
    private boolean isPaid;
    private UserResponseDto user;
    private FutsalResponseDTO futsal;
    private UserResponseDto owner;
    private Long price;
}
