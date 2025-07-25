package com.codewithgaurav.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/*
 * here, UserModel is the name of the java class of model and string is the data type of the primary key 
 */

import com.codewithgaurav.store.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
   UserEntity findByUsername(String username);
}
