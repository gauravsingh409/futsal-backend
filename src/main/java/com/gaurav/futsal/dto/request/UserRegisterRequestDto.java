package com.gaurav.futsal.dto.request;

import lombok.Data;

@Data
public class UserRegisterRequestDto {
    private String firstName;
    private String lastName;
    private String email;
    private String profile;
    private String provider;

}
