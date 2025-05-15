package com.codewithgaurav.store.controller;

import com.codewithgaurav.store.model.FootsalLocationModel;
import com.codewithgaurav.store.services.FootsalLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/footsal")
public class FootsalLocationController {

   @Autowired
   private FootsalLocationService footsalService;

   // Add new futsal location
   @PostMapping
   public ResponseEntity<FootsalLocationModel> addLocation(@RequestBody FootsalLocationModel location) {
      FootsalLocationModel savedLocation = footsalService.saveLocation(location);
      return ResponseEntity.ok(savedLocation);
   }

   // Get nearest futsal locations
   @GetMapping("/nearest")
   public ResponseEntity<List<FootsalLocationModel>> getNearestLocations(
         @RequestParam double lat,
         @RequestParam double lon,
         @RequestParam(defaultValue = "5") double radiusKm) {
      System.out.println("Latitude" + lat + " longitude" + lon + "radius " + radiusKm);

      List<FootsalLocationModel> locations = footsalService.findNearestLocations(lat, lon, radiusKm);
      return ResponseEntity.ok(locations);
   }
}
