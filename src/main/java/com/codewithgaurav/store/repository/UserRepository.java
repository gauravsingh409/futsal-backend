package com.codewithgaurav.store.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.codewithgaurav.store.model.UserModel;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> { // here, UserModel is name of java class of
                                                                             // model and string is data type of primary
                                                                             // key
   UserModel findByUsername(String username);

}
