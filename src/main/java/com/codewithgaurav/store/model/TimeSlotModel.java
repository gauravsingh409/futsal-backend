package com.codewithgaurav.store.model;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.annotation.Id;

public class TimeSlotModel {
   @Id
   private String id;

   private String futsalId; // Reference to the Futsal document
   private LocalDate date;
   private LocalTime startTime;
   private LocalTime endTime;
   private Double price;
   private boolean isBooked = false;

   // Getter and Setter
   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFutsalId() {
      return futsalId;
   }

   public void setFutsalId(String futsalId) {
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

   public void setBooked(boolean isBooked) {
      this.isBooked = isBooked;
   }

}
