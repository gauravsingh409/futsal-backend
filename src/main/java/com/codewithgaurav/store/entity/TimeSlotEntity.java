package com.codewithgaurav.store.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "time_slots") // Table name in PostgreSQL
public class TimeSlotEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private LocalTime startTime;
   private LocalTime endTime;
   private Double price;

   @ManyToOne
   @JoinColumn(name = "futsal_id", nullable = false)
   private FutsalEntity futsal;

   // Getters and Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
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

   public FutsalEntity getFutsal() {
      return futsal;
   }

   public void setFutsal(FutsalEntity futsal) {
      this.futsal = futsal;
   }
}