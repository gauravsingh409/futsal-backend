package com.codewithgaurav.store.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codewithgaurav.store.model.OwnerModel;

@Repository
public interface OwnerRepository extends MongoRepository<OwnerModel, String> {
   OwnerModel findByUsername(String username);

}