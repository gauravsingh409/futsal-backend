package com.codewithgaurav.store.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponseDto {
    private Long id;
    private String customerName;
    private String phone;
    private LocalDate bookedDate;
    private String status;
    private LocalDateTime createAt;
    private boolean isPaid;
    private UserDto user; // mapped using UserMapper
    private FutsalResponseDTO futsal; // mapped using FutsalMapper
    private UserDto owner; // mapped using UserMapper
    private Long price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBookedDate() {
        return bookedDate;
    }

    public void setBookedDate(LocalDate bookedDate) {
        this.bookedDate = bookedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean isPaid) {
        this.isPaid = isPaid;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public FutsalResponseDTO getFutsal() {
        return futsal;
    }

    public void setFutsal(FutsalResponseDTO futsal) {
        this.futsal = futsal;
    }

    public UserDto getOwner() {
        return owner;
    }

    public void setOwner(UserDto owner) {
        this.owner = owner;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }
}
