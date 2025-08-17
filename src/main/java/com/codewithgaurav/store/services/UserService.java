package com.codewithgaurav.store.services;

import java.io.File;

import com.codewithgaurav.store.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithgaurav.store.dto.request.UserRequestDto;
import com.codewithgaurav.store.dto.response.UserDto;
import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.exception.ResourceNotFoundException;
import com.codewithgaurav.store.mapper.UserMapper;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    public UserDto findDetailsById(Long id) {
        UserEntity owner = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return this.convertToUserDto(owner);
    }

    public UserEntity getUserDetailsById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", id));
        return user;
    }

    public UserEntity updateUserProfile(Long id, UserEntity request) throws RuntimeException {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        if (request.getEmail() != null && !request.getEmail().isEmpty())
            user.setEmail(request.getEmail());
        if (request.getAddress() != null && !request.getAddress().isEmpty())
            user.setAddress(request.getAddress());
        if (request.getPhoneNo() != null && !request.getPhoneNo().isEmpty())
            user.setPhoneNo(request.getPhoneNo());

        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            String oldFilePath = System.getProperty("user.dir") + user.getProfilePicture();
            File oldFile = new File(oldFilePath);
            if (oldFile.exists()) {
                boolean deleted = oldFile.delete();
                if (deleted) {
                    user.setProfilePicture(request.getProfilePicture());
                } else {
                    String newFilePath = System.getProperty("user.dir") + request.getProfilePicture();
                    File newFile = new File(newFilePath);
                    if (newFile.delete()) {
                        throw new RuntimeException("some error occurred");
                    }
                }
            } else {
                user.setProfilePicture(request.getProfilePicture());
            }
        }
        return userRepository.save(user);
    }

    public UserEntity updateOwnerDetails(Long id, UserRequestDto request) {
        UserEntity existingOwner = userRepository.findById(id)
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

        // Profile Picture
        if (request.getProfileImageUrl() != null && !request.getProfileImageUrl().isEmpty()) {
            String oldImagePath = System.getProperty("user.dir") + existingOwner.getProfilePicture();
            File oldFile = new File(oldImagePath);
            if (oldFile.exists()) {
                oldFile.delete();
            }
            existingOwner.setProfilePicture(request.getProfileImageUrl());
        }

        // save
        return userRepository.save(existingOwner);
    }

    // Convert UserEntity to UserDot
    public UserDto convertToUserDto(UserEntity user) {
        return userMapper.toDto(user);
    }

    // delete the user
    public boolean deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }
}
