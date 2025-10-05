package com.gaurav.futsal.services;

import com.gaurav.futsal.dto.response.UserDto;
import com.gaurav.futsal.entity.UserEntity;
import com.gaurav.futsal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final Keycloak keycloak;

    public UserDto createUser(UserEntity request) {
        UserRepresentation userRepresentation;
        if (userRepo.existsByKeycloakId(request.getKeycloakId())) {
            userRepresentation = keycloak
                    .realm("futsal-realm")
                    .users()
                    .get(request.getKeycloakId())
                    .toRepresentation();
            return getUserDto(userRepresentation);
        }
        UserEntity savedUser = userRepo.save(request);
        userRepresentation = keycloak
                .realm("futsal-realm")
                .users()
                .get(request.getKeycloakId())
                .toRepresentation();
        return getUserDto(userRepresentation);
    }

    private UserDto getUserDto(UserRepresentation userRepresentation) {
        return UserDto.builder()
                .keyCloakId(userRepresentation.getId())
                .username(userRepresentation.getUsername())
                .email(userRepresentation.getEmail())
                .emailVerified(userRepresentation.isEmailVerified())
                .firstName(userRepresentation.getFirstName())
                .lastName(userRepresentation.getLastName())
                .fullName(userRepresentation.getFirstName() + " " + userRepresentation.getLastName())
                .build();
    }
}
