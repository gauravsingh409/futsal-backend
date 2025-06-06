package com.codewithgaurav.store.repository.futsal;

import com.codewithgaurav.store.model.FutsalModel;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface FutsalRepository extends MongoRepository<FutsalModel, String> {
   Optional<FutsalModel> findByRegistrationNumber(String futsalRegistrationNumber);

   boolean existsByRegistrationNumber(String registrationNumber); // check does this exists in database or not

}
