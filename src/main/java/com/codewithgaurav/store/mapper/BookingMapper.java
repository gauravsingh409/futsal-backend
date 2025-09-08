package com.codewithgaurav.store.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.codewithgaurav.store.dto.response.BookingResponseDto;
import com.codewithgaurav.store.entity.BookingEntity;

@Mapper(componentModel = "spring", uses = { FutsalMapper.class, UserMapper.class })
public abstract class BookingMapper {

    @Mapping(source = "bookedDate", target = "bookedDate")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "futsal", target = "futsal")
    @Mapping(source = "owner", target = "owner")
    public abstract BookingResponseDto toDto(BookingEntity bookingEntity);

}
