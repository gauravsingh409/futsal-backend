package com.codewithgaurav.store.services;

import com.codewithgaurav.store.model.FootsalLocationModel;
import com.codewithgaurav.store.repository.FootsalLocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FootsalLocationService {

   @Autowired
   private FootsalLocationRepository repository;

   // Save new futsal location
   public FootsalLocationModel saveLocation(FootsalLocationModel location) {
      return repository.save(location);
   }

   // Find nearest locations within radius (in KM)
   public List<FootsalLocationModel> findNearestLocations(double latitude, double longitude, double radiusKm) {
      GeoJsonPoint userLocation = new GeoJsonPoint(longitude, latitude); // Note: longitude first
      Distance distance = new Distance(radiusKm, Metrics.KILOMETERS); // Define search radius
      return repository.findByLocationNear(userLocation, distance);
   }
}
