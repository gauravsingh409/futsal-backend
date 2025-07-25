package com.codewithgaurav.store.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots") // Table name in PostgreSQL
public class TimeSlotEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "futsal_id", nullable = false)
   private Long futsalId; // Foreign key reference (can be linked later)

   private LocalDate date;
   private LocalTime startTime;
   private LocalTime endTime;
   private Double price;

   @Column(name = "is_booked")
   private boolean isBooked = false;

   // Getters and Setters

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getFutsalId() {
      return futsalId;
   }

   public void setFutsalId(Long futsalId) {
      this.futsalId = futsalId;
   }

   public LocalDate getDate() {
      return date;
   }

   public void setDate(LocalDate date) {
      this.date = date;
   }

   public LocalTime getStartTime() {
      return startTime;
   }

   public void setStartTime(LocalTime startTime) {
      this.startTime = startTime;
   }

   public LocalTime getEndTime() {
      return endTime;
   }

   public void setEndTime(LocalTime endTime) {
      this.endTime = endTime;
   }

   public Double getPrice() {
      return price;
   }

   public void setPrice(Double price) {
      this.price = price;
   }

   public boolean isBooked() {
      return isBooked;
   }

   public void setBooked(boolean booked) {
      isBooked = booked;
   }
}