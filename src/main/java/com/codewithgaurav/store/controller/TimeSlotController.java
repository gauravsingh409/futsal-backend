package com.codewithgaurav.store.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.codewithgaurav.store.entity.TimeSlotEntity;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.services.FutsalService;
import com.codewithgaurav.store.services.JwtService;
import com.codewithgaurav.store.services.TimeSlotService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/time-slot")
public class TimeSlotController {

   @Autowired
   private TimeSlotService timeSlotService;

   @Autowired
   private JwtService jwtService;

   @Autowired
   FutsalService futsalService;

   @PostMapping(value = "/create")
   public ResponseEntity<?> createSlot(
         @RequestBody TimeSlotEntity timeslot,
         HttpServletRequest httpRequest) {
      jwtService.isValidAdminOrOwner(httpRequest);
      // check the time slot exist on daabase or not
      boolean isTimeSlotExist = timeSlotService.isTimeSlotExist(timeslot.getFutsalId(), timeslot.getStartTime(),
            timeslot.getEndTime());
      if (isTimeSlotExist)
         return ResponseEntity.badRequest().body(new ApiResponse<>("Time slot already exists", 400, false));

      // process further the details
      TimeSlotEntity data = timeSlotService.createTimeSlot(timeslot);
      return ResponseEntity.ok()
            .body(new ApiResponse<TimeSlotEntity>("Timeslot created successfully", 201, true, data));
   }

   // Get all the time-slot by futsal id
   @GetMapping(value = "/get-all/{futsalId}")
   public ResponseEntity<?> getAllTimeSlot(@PathVariable Long futsalId, HttpServletRequest httpServletRequest) {
      List<TimeSlotEntity> slots = timeSlotService.getSlotsByFutsal(futsalId);
      return ResponseEntity.ok().body(new ApiResponse<>("Time slot retrieved successfully", 200, true, slots));
   }

   // get time-slot details by id
   @GetMapping(value = "/details/{timeSlotId}")
   public ResponseEntity<?> getTimeSlotDetails(@PathVariable Long timeSlotId, HttpServletRequest httpServletRequest) {
      TimeSlotEntity timeSlot = timeSlotService.getDetailsById(timeSlotId);
      return ResponseEntity.ok().body(new ApiResponse<>("Timeslot Details retrived", 200, true, timeSlot));
   }

   @PutMapping("/details/{timeSlotId}")
   public ResponseEntity<?> updateTimeSlot(@PathVariable Long timeSlotId,
         HttpServletRequest httpServletRequest,
         @RequestBody TimeSlotEntity request) {
      Long id = jwtService.extractId(httpServletRequest);

      TimeSlotEntity data = timeSlotService.putUpdate(request, timeSlotId);

      return ResponseEntity.ok().body(new ApiResponse<>("Time slot updated successfully", 200, true, data));
   }

   @DeleteMapping(value = "/delete/{id}")
   public ResponseEntity<?> deleteTimeSlot(@PathVariable("id") Long timeSlotId, HttpServletRequest httpServletRequest) {
      jwtService.isValidAdminOrOwner(httpServletRequest);
      if (timeSlotService.deleteTimeSlot(timeSlotId))
         return ResponseEntity.ok().body(new ApiResponse<>("Time slot deleted Successfully", 200, true));
      return ResponseEntity.ok().body(new ApiResponse<>("Time slot with id " + timeSlotId + " not found", 400, false));
   }

}
