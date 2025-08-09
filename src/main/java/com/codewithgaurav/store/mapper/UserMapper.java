package com.codewithgaurav.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithgaurav.store.dto.response.UserDto;
import com.codewithgaurav.store.entity.UserEntity;

@Mapper(componentModel = "spring")
public interface UserMapper {
   @Mapping(source = "phoneNo", target = "phone_no", ignore = true)
   @Mapping(source = "profilePicture", target = "profileImageUrl", ignore = true)
   UserDto toDto(UserEntity user);
}
