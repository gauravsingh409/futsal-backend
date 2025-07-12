package com.codewithgaurav.store.repository.profile;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.codewithgaurav.store.model.UserModel;

@Repository
public interface OwnerCompleteProfileRepository extends MongoRepository<UserModel, String> {

}