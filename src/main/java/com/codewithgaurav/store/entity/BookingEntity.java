package com.codewithgaurav.store.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "booking")
public class BookingEntity {

    public enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String phone;

    private LocalDate bookedDate;
    private LocalTime bookedTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private LocalDateTime createAt;
    private boolean isPaid;

    // Many bookings can belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // Many bookings belong to one futsal ground
    @ManyToOne
    @JoinColumn(name = "futsal")
    private FutsalEntity futsal;

    @ManyToOne
    @JoinColumn(name = "owner")
    private UserEntity owner;

    private Long price;

    @PrePersist
    public void PrePersist() {
        createAt = LocalDateTime.now();
        if (status == null)
            status = BookingStatus.PENDING;
    }

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

    public LocalTime getBookedTime() {
        return bookedTime;
    }

    public void setBookedTime(LocalTime bookedTime) {
        this.bookedTime = bookedTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public FutsalEntity getFutsal() {
        return futsal;
    }

    public void setFutsal(FutsalEntity futsal) {
        this.futsal = futsal;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

}
