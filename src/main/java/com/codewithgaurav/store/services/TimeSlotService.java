package com.codewithgaurav.store.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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

   // get details by id
   public TimeSlotModel getDetailsById(String timeSlotId) {
      Optional<TimeSlotModel> data = timeSlotRepo.findById(timeSlotId);
      return data.get();
   }

   // Time SLot Put Update
   public TimeSlotModel putUpdate(TimeSlotModel request, String timeSlotId) {
      Optional<TimeSlotModel> existingOptional = timeSlotRepo.findById(timeSlotId);
      if (existingOptional.isEmpty())
         throw new ResourceNotFoundException("Time Slot with Id " + timeSlotId + "not found");
      // Update the data
      existingOptional.get().setDate(request.getDate());
      existingOptional.get().setStartTime(request.getStartTime());
      existingOptional.get().setEndTime(request.getEndTime());
      existingOptional.get().setPrice(request.getPrice());
      timeSlotRepo.save(existingOptional.get());
      return existingOptional.get();
   }

}
