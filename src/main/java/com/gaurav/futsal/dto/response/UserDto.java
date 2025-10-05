package com.gaurav.futsal.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String keyCloakId;
    private String username;
    private String email;
    private boolean emailVerified;
    private String firstName;
    private String lastName;
    private String fullName;
}
