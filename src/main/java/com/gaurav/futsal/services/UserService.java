package com.gaurav.futsal.services;

import com.gaurav.futsal.dto.request.UserRegisterRequestDto;
import com.gaurav.futsal.dto.response.UserResponseDto;
import com.gaurav.futsal.entity.UserEntity;
import com.gaurav.futsal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    //    Checks the user is already exist or not
    public UserResponseDto getUserDetails(String email) {
        UserEntity user = userRepository.findByEmail(email);
        return this.userEntityToUserResponseDto(user);
    }

    public UserResponseDto registerUser(UserRegisterRequestDto request) {
        UserEntity user = UserEntity.builder().email(request.getEmail()).first_name(request.getFirstName()).last_name(request.getLastName()).profile(request.getProfile()).provider_name(request.getProvider()).build();
        UserEntity savedUser = userRepository.save(user);
        return this.userEntityToUserResponseDto(savedUser);
    }


    private UserResponseDto userEntityToUserResponseDto(UserEntity user) {
        if (user == null) return null;
        return UserResponseDto.builder().email(user.getEmail()).firstName(user.getFirst_name()).lastName(user.getLast_name()).profile(user.getProfile()).build();
    }

}
