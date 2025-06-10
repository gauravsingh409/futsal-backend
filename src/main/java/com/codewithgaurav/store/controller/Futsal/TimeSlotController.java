package com.codewithgaurav.store.controller.Futsal;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.model.TimeSlotModel;
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
         @RequestBody TimeSlotModel timeslot,
         HttpServletRequest httpRequest) {
      String authHeader = httpRequest.getHeader("Authorization");
      if (authHeader == null || !authHeader.startsWith("Bearer")) {
         ApiResponse<Map<String, Object>> response = new ApiResponse<>("user not authenticated", 401, false);
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
      }
      String token = authHeader.substring(7);
      String id = jwtService.extractId(token);
      boolean isOwner = futsalService.isOwner(id);

      // if not owner return false
      if (!isOwner) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
               .body(new ApiResponse<>("Permission not allowed", 401, false));
      }
      // check the time slot exist on daabase or not
      boolean isTimeSlotExist = timeSlotService.isTimeSlotExist(timeslot.getFutsalId(), timeslot.getDate(),
            timeslot.getStartTime());

      if (isTimeSlotExist)
         return ResponseEntity.badRequest().body(new ApiResponse<>("Time slot already exists", 400, false));

      // process further the details
      TimeSlotModel data = timeSlotService.createTimeSlot(timeslot);
      return ResponseEntity.ok().body(new ApiResponse<TimeSlotModel>("Timeslot created successfully", 201, true, data));
   }

   // Get all the time-slot by futsal id
   @GetMapping(value = "/get-all/{futsalId}")
   public ResponseEntity<?> getAllTimeSlot(@PathVariable String futsalId, HttpServletRequest httpServletRequest) {
      List<TimeSlotModel> slots = timeSlotService.getSlotsByFutsal(futsalId);
      return ResponseEntity.ok().body(new ApiResponse<>("Time slot retrieved successfully", 200, true, slots));
   }

}
