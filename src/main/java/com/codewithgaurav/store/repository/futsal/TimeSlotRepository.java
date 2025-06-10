package com.codewithgaurav.store.repository.futsal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.codewithgaurav.store.model.TimeSlotModel;

public interface TimeSlotRepository extends MongoRepository<TimeSlotModel, String> {
   List<TimeSlotModel> findByFutsalId(String futsalId);

   boolean existsByFutsalIdAndDateAndStartTime(String FutsalId, LocalDate date, LocalTime starTime);
}
