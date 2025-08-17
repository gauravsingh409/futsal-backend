package com.codewithgaurav.store.dto.response;

import java.time.LocalTime;

public class TimeSlotResponseDto {

 private Long id;
 private Long futsalId;
 private String futsalName;
 private LocalTime startTime;
 private LocalTime endTime;
 private Double price;

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

 public String getFutsalName() {
  return futsalName;
 }

 public void setFutsalName(String futsalName) {
  this.futsalName = futsalName;
 }

}
