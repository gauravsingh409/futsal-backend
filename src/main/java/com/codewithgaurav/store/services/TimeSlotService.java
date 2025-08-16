package com.codewithgaurav.store.services;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.codewithgaurav.store.entity.TimeSlotEntity;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.repository.FutsalRepository;
import com.codewithgaurav.store.repository.TimeSlotRepository;

@Service
public class TimeSlotService {

   @Autowired
   private TimeSlotRepository timeSlotRepo;

   @Autowired
   private FutsalRepository futsalRepo;

   // Save the time slot
   public TimeSlotEntity createTimeSlot(TimeSlotEntity timeSlot) {
      try {
         return timeSlotRepo.save(timeSlot);
      } catch (Exception e) {
         throw new RuntimeException("Failed to save time slot: " + e.getMessage());
      }
   }

   // get the time slot
   public List<TimeSlotEntity> getSlotsByFutsal(Long futsalId) {
      boolean isFutsalExist = futsalRepo.existsById(futsalId);
      if (!isFutsalExist)
         throw new ResourceNotFoundException("futsal with id " + futsalId + " not found");
      return timeSlotRepo.findByFutsalId(futsalId);
   }

   // Is Futsal TimeSlot exist
   public boolean isTimeSlotExist(Long futsalId, LocalTime startTime, LocalTime endTime) {
      System.out.println("startTime " + startTime);
      System.out.println("endTime " + endTime);
      return timeSlotRepo.existsByFutsalIdAndStartTime(futsalId, startTime);
   }

   // get details by id
   public TimeSlotEntity getDetailsById(Long timeSlotId) {
      Optional<TimeSlotEntity> data = timeSlotRepo.findById(timeSlotId);
      return data.get();
   }

   // Time SLot Put Update
   public TimeSlotEntity putUpdate(TimeSlotEntity request, Long timeSlotId) {
      Optional<TimeSlotEntity> existingOptional = timeSlotRepo.findById(timeSlotId);
      if (existingOptional.isEmpty())
         throw new ResourceNotFoundException("Time Slot with Id " + timeSlotId + "not found");
      // Update the data
      existingOptional.get().setStartTime(request.getStartTime());
      existingOptional.get().setEndTime(request.getEndTime());
      existingOptional.get().setPrice(request.getPrice());
      timeSlotRepo.save(existingOptional.get());
      return existingOptional.get();
   }

   // Delete Time Slot
   public boolean deleteTimeSlot(Long timeSlotId) {
      if (timeSlotRepo.existsById(timeSlotId)) {
         timeSlotRepo.deleteById(timeSlotId);
         return true;
      }
      return false;
   }

}
