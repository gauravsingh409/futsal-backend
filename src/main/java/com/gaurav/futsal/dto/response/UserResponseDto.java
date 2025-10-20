package com.gaurav.futsal.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
}
