package com.gaurav.futsal.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class FutsalDetailsDto {
   private String id;
   private String name;
   private String registrationNumber;
   private String district;
   private String city;
   private List<String> images;
   private String coverImage;
   private String registrationPhoto;
   private UserResponseDto user;
   private Date createdAt;
   private Date updatedAt;

}
