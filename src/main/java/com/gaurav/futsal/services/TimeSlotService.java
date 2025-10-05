package com.gaurav.futsal.services;

import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.gaurav.futsal.dto.response.TimeSlotResponseDto;
import com.gaurav.futsal.entity.FutsalEntity;
import com.gaurav.futsal.entity.TimeSlotEntity;
import com.gaurav.futsal.exception.ResourceNotFoundException;
import com.gaurav.futsal.repository.FutsalRepository;
import com.gaurav.futsal.repository.TimeSlotRepository;

@Service
public class TimeSlotService {

   @Autowired
   private TimeSlotRepository timeSlotRepo;

   @Autowired
   private FutsalRepository futsalRepo;


   // Save the time slot
   public TimeSlotEntity createTimeSlot(TimeSlotEntity timeSlot, Long futsalId) {
      FutsalEntity futsal = futsalRepo.findById(futsalId).get();
      try {
         timeSlot.setFutsal(futsal);
         return timeSlotRepo.save(timeSlot);
      } catch (Exception e) {
         throw new RuntimeException("Failed to save time slot: " + e.getMessage());
      }
   }

   // get the duration
   public Long getTimeDuration(LocalTime starTime, LocalTime endTime) {
      Duration duration = Duration.between(starTime, endTime);
      return duration.toMinutes();
   }

   // get the time slot
   public List<TimeSlotEntity> getSlotsByFutsal(Long futsalId) {
      boolean isFutsalExist = futsalRepo.existsById(futsalId);
      if (!isFutsalExist)
         throw new ResourceNotFoundException("futsal with id " + futsalId + " not found");
      return timeSlotRepo.findByFutsalId(futsalId);
   }

   public Page<TimeSlotEntity> getPaginatedTimeSlotList(String search, Long futsalId, Pageable pageable) {
      return timeSlotRepo.findByPriceAndFutsalId(search, futsalId, pageable);
   }

   // Is Futsal TimeSlot exist
   public boolean isSlotOverlap(LocalTime starTime, LocalTime endTime, Long userId) {
      List<TimeSlotEntity> timeSlotList = this.getSlotsByFutsal(userId);
      boolean isOverlap = timeSlotList.stream()
            .anyMatch(slot -> starTime.isBefore(slot.getEndTime()) && slot.getStartTime().isBefore(endTime));
      return isOverlap;
   }

   // get details by id
   public TimeSlotEntity getDetailsById(Long timeSlotId) {
      Optional<TimeSlotEntity> data = timeSlotRepo.findById(timeSlotId);
      return data.get();
   }

   // Time SLot Put Update
   public TimeSlotEntity putUpdate(TimeSlotEntity request, Long timeSlotId) {
      TimeSlotEntity existingData = this.getDetailsById(timeSlotId);
      existingData.setStartTime(request.getStartTime());
      existingData.setEndTime(request.getEndTime());
      existingData.setPrice(request.getPrice());
      return timeSlotRepo.save(existingData);
   }

   // Delete Time Slot
   public boolean deleteTimeSlot(Long timeSlotId) {
      if (timeSlotRepo.existsById(timeSlotId)) {
         timeSlotRepo.deleteById(timeSlotId);
         return true;
      }
      return false;
   }

   // conver to dto
   public TimeSlotResponseDto getTimeSlotResponseDto(TimeSlotEntity timeSlotEntity) {
       return new TimeSlotResponseDto();
   }

   // convert timeslot list to dto
   public List<TimeSlotResponseDto> convertEntityPageToDto(Page<TimeSlotEntity> timeSlotPage) {
      return timeSlotPage.stream().map(this::getTimeSlotResponseDto).toList();
   }

}
