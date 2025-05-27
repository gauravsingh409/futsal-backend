package com.codewithgaurav.store.repository.profile;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.codewithgaurav.store.model.OwnerModel;

@Repository
public interface OwnerCompleteProfileRepository extends MongoRepository<OwnerModel, String> {

}