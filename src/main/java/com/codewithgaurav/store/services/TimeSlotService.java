package com.codewithgaurav.store.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.model.TimeSlotModel;
import com.codewithgaurav.store.repository.futsal.FutsalRepository;
import com.codewithgaurav.store.repository.futsal.TimeSlotRepository;

@Service
public class TimeSlotService {

   @Autowired
   private TimeSlotRepository timeSlotRepo;

   @Autowired
   private FutsalRepository futsalRepo;

   // Save the time slot
   public TimeSlotModel createTimeSlot(TimeSlotModel timeSlot) {
      try {
         return timeSlotRepo.save(timeSlot);
      } catch (Exception e) {
         throw new RuntimeException("Failed to save time slot: " + e.getMessage());
      }
   }

   // get the time slot
   public List<TimeSlotModel> getSlotsByFutsal(String futsalId) {
      boolean isFutsalExist = futsalRepo.existsById(futsalId);
      if (!isFutsalExist)
         throw new ResourceNotFoundException("futsal with id " + futsalId + " not found");
      return timeSlotRepo.findByFutsalId(futsalId);
   }

   // Is Futsal TimeSlot exist
   public boolean isTimeSlotExist(String futsalId, LocalDate date, LocalTime startTime) {
      return timeSlotRepo.existsByFutsalIdAndDateAndStartTime(futsalId, date, startTime);
   }

}
