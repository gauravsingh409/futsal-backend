package com.codewithgaurav.store.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codewithgaurav.store.model.FootsalModel;

@Repository
public interface FootsalRepository extends MongoRepository<FootsalModel, String> {
   FootsalModel findByUsername(String username);

}