package com.codewithgaurav.store.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;

import com.codewithgaurav.store.dto.response.FutsalResponseDTO;
import com.codewithgaurav.store.entity.FutsalEntity;
import com.codewithgaurav.store.entity.FutsalImages;

@Mapper(componentModel = "spring")
public interface FutsalMapper {

   FutsalResponseDTO toDto(FutsalEntity futsal);

   default String mapFutsalImageToString(FutsalImages image) {
      return image.getImageUrl();
   }

   default List<String> mapFutsalImageToList(List<FutsalImages> images) {
      return images.stream()
            .map(this::mapFutsalImageToString)
            .collect(Collectors.toList());
   }

}
