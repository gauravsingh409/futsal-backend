package com.codewithgaurav.store.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.dto.response.TimeSlotResponseDto;
import com.codewithgaurav.store.entity.TimeSlotEntity;
import com.codewithgaurav.store.payload.ApiResponse;
import com.codewithgaurav.store.payload.PaginatedResponse;
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

   @PostMapping(value = "/create/{futsalId}")
   public ResponseEntity<?> createSlot(
         @PathVariable Long futsalId,
         @RequestBody TimeSlotEntity request,
         HttpServletRequest httpRequest) {
      // accept only admin or owner request
      jwtService.isValidAdminOrOwner(httpRequest);
      Long userId = jwtService.extractId(httpRequest);
      // does owner own this futsal or not
      boolean isFutsalUserIdExist = futsalService.existByUserIdAndFutsalId(futsalId, userId);
      if (!isFutsalUserIdExist)
         return ResponseEntity.badRequest().body(new ApiResponse<>("You don't own this futsal", 400, false));
      // Duration must be at least 30 minutes
      Long duration = timeSlotService.getTimeDuration(request.getStartTime(), request.getEndTime());
      if (duration < 30)
         return ResponseEntity.badRequest().body(new ApiResponse<>("Duration must be at least 30 minutes", 400, false));
      // Does this time overlap other slot
      boolean isTimeSlotExist = timeSlotService.isSlotOverlap(request.getStartTime(), request.getEndTime(),
            futsalId);
      if (isTimeSlotExist)
         return ResponseEntity.badRequest().body(new ApiResponse<>("Time slot already exists", 400, false));
      // create entry to database & convert to dto
      TimeSlotResponseDto data = timeSlotService.convertToDto(timeSlotService.createTimeSlot(request, futsalId));
      return ResponseEntity.ok()
            .body(new ApiResponse<TimeSlotResponseDto>("Timeslot created successfully", 201, true, data));
   }

   // Get all the time-slot by futsal id
   @GetMapping(value = "/get-all/{futsalId}")
   public ResponseEntity<?> getAllTimeSlot(
         @PathVariable Long futsalId,
         HttpServletRequest httpServletRequest,
         @RequestParam(defaultValue = "0", name = "page") int page,
         @RequestParam(defaultValue = "10", name = "pageSize") int pageSize,
         @RequestParam(required = false, defaultValue = "") String search) {

      Pageable pageable = PageRequest.of(page, pageSize);
      Page<TimeSlotEntity> timeSlotPage = timeSlotService.getPaginatedTimeSlotList(search, futsalId, pageable);
      List<TimeSlotResponseDto> timeSlotListDto = timeSlotService.convertEntityPageToDto(timeSlotPage);
      var response = new PaginatedResponse<>(timeSlotListDto, timeSlotPage.getNumber(), timeSlotPage.getSize(),
            timeSlotPage.getTotalElements(), timeSlotPage.getTotalPages(),
            timeSlotPage.getNumber() * timeSlotPage.getSize() + 1);

      return ResponseEntity.ok()
            .body(new ApiResponse<>("Time slot retrieved successfully", 200, true, response));
   }

   // get time-slot details by id
   @GetMapping(value = "/details/{timeSlotId}")
   public ResponseEntity<?> getTimeSlotDetails(@PathVariable Long timeSlotId, HttpServletRequest httpServletRequest) {
      TimeSlotEntity timeSlot = timeSlotService.getDetailsById(timeSlotId);
      TimeSlotResponseDto timeSlotResponseDto = timeSlotService.convertToDto(timeSlot);
      return ResponseEntity.ok().body(new ApiResponse<>("Timeslot Details retrived", 200, true, timeSlotResponseDto));
   }

   @PutMapping("/update/{timeSlotId}")
   public ResponseEntity<?> updateTimeSlot(@PathVariable Long timeSlotId,
         HttpServletRequest httpServletRequest,
         @RequestBody TimeSlotEntity request) {
      TimeSlotEntity data = timeSlotService.putUpdate(request, timeSlotId);
      TimeSlotResponseDto timeSlotResponseDto = timeSlotService.convertToDto(data);
      return ResponseEntity.ok().body(new ApiResponse<>("Time slot updated successfully", 200, true,
            timeSlotResponseDto));
   }

   @DeleteMapping(value = "/delete/{id}")
   public ResponseEntity<?> deleteTimeSlot(@PathVariable("id") Long timeSlotId, HttpServletRequest httpServletRequest) {
      jwtService.isValidAdminOrOwner(httpServletRequest);
      if (timeSlotService.deleteTimeSlot(timeSlotId))
         return ResponseEntity.ok().body(new ApiResponse<>("Time slot deleted Successfully", 200, true));
      return ResponseEntity.ok().body(new ApiResponse<>("Time slot with id " + timeSlotId + " not found", 400, false));
   }

}
