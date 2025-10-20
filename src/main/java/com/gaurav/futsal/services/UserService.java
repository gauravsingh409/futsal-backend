package com.gaurav.futsal.services;

import com.gaurav.futsal.dto.response.UserResponseDto;
import com.gaurav.futsal.entity.UserEntity;
import com.gaurav.futsal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDto getUserDetails(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return this.userEntityToUserResponseDto(user);
    }

    private UserResponseDto userEntityToUserResponseDto(UserEntity user) {
        if (user == null) return null;
        return UserResponseDto.builder().email(user.getEmail()).firstName(user.getFirst_name()).lastName(user.getLast_name()).build();
    }

}
