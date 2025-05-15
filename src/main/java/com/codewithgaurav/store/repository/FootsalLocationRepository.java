package com.codewithgaurav.store.repository;

import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.codewithgaurav.store.model.FootsalLocationModel;

public interface FootsalLocationRepository extends MongoRepository<FootsalLocationModel, String> {

   // Find locations near a given point within a certain distance
   List<FootsalLocationModel> findByLocationNear(GeoJsonPoint location, Distance distance);
}
