package com.codewithgaurav.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithgaurav.store.dto.response.TimeSlotResponseDto;
import com.codewithgaurav.store.entity.TimeSlotEntity;

@Mapper(componentModel = "spring")
public interface TimeSlotMapper {

 @Mapping(target = "futsalId", source = "futsal.id")
 @Mapping(target = "futsalName", source = "futsal.name")
 public abstract TimeSlotResponseDto toDto(TimeSlotEntity timeSlotEntity);

}
