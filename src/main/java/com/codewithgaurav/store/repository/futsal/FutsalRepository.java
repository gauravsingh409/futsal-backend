package com.codewithgaurav.store.repository.futsal;

import com.codewithgaurav.store.model.FutsalModel;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.lang.NonNull;

public interface FutsalRepository extends MongoRepository<FutsalModel, String> {
   Optional<FutsalModel> findByRegistrationNumber(String futsalRegistrationNumber);

   boolean existsByRegistrationNumber(String registrationNumber); // check does this exists in database or not

   Page<FutsalModel> findByOwner_Id(String ownerId, @NonNull Pageable pageable);

   // Get all the futsals with pagination
   @NonNull
   Page<FutsalModel> findAll(@NonNull Pageable pageable);

   // Search By Keyword
   @Query("""
            {
               "$or":[
                  {"name":{"$regex":?0, "$options":"i"}},
                  {"city":{"$regex":?0, "$options":"i"}},
                  {"owner":{"$regex":?0, "$options":"i"}},
               ]
            }
         """)
   @NonNull
   Page<FutsalModel> searchByKeyword(@NonNull String keyword, @NonNull Pageable pageable);

}
