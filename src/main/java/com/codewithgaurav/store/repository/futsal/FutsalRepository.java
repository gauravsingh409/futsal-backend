package com.codewithgaurav.store.repository.futsal;

import com.codewithgaurav.store.model.FutsalModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FutsalRepository extends MongoRepository<FutsalModel, String> {

}
