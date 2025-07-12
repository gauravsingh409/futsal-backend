package com.codewithgaurav.store.services;

import java.io.File;

import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.repository.profile.OwnerCompleteProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {

    @Autowired
    OwnerCompleteProfileRepository repo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    ObjectMapper objectMapper;

    public String findDetailsById(String id) throws JsonProcessingException {
        UserModel owner = repo.findById(id)
                // .get();
                .orElseThrow(() -> new ResourceNotFoundException("owner", "id", id));
        // System.out.println(owner); // it will give the java object memory reference
        // ObjectMapper translate the java object to the json and json to java object.
        return objectMapper.writeValueAsString(owner);
    }

    public UserModel updateUserProfile(String id, UserModel request) throws RuntimeException {
        UserModel user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());
        if (request.getAddress() != null && !request.getAddress().isEmpty())
            user.setAddress(request.getAddress());
        if (request.getPhone_no() != null && !request.getPhone_no().isEmpty())
            user.setPhone_no(request.getPhone_no());

        if (user.getProfile_picture() != null && !user.getProfile_picture().isEmpty()) {
            String oldFilePath = System.getProperty("user.dir") + user.getProfile_picture();
            File oldFile = new File(oldFilePath);
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                if (deleted) {
                    user.setProfile_picture(request.getProfile_picture());
                } else {
                    String newFilePath = System.getProperty("user.dir") + request.getProfile_picture();
                    File newFile = new File(newFilePath);
                    if (newFile.delete()) {
                        throw new RuntimeException("some error occurred");
                    }
                }
            } else {
                user.setProfile_picture(request.getProfile_picture());
            }
        }
        return userRepo.save(user);
    }

    public UserModel updateOwnerDetails(String id, UserRequestDto request) {
        UserModel existingOwner = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner", "id", id));

        // update field
        // FullName
        if (request.getFullName() != null && !request.getFullName().isEmpty())
            existingOwner.setFullName(request.getFullName());

        // Email
        if (request.getEmail() != null && !request.getEmail().isEmpty())
            existingOwner.setEmail(request.getEmail());

        // Date Of Birth
        if (request.getDateOfBirth() != null)
            existingOwner.setDateOfBirth(request.getDateOfBirth());

        // Emergency Number
        if (request.getEmergencyContact() != null && !request.getEmergencyContact().isEmpty())
            existingOwner.setEmergencyContact(request.getEmergencyContact());

        // Profile Picture
        if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isEmpty()) {
            String oldImagePath = System.getProperty("user.dir") + existingOwner.getProfileImageUrl();
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            existingOwner.setProfileImageUrl(request.getProfileImageUrl());
        }

        // save
        return repo.save(existingOwner);
    }

}
