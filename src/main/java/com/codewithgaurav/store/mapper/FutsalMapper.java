package com.codewithgaurav.store.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Value;
import com.codewithgaurav.store.dto.response.FutsalResponseDTO;
import com.codewithgaurav.store.entity.FutsalEntity;
import com.codewithgaurav.store.entity.FutsalImages;

@Mapper(componentModel = "spring")
public abstract class FutsalMapper {

   @Value("${app.base-url}")
   protected String baseUrl;

   @Mapping(target = "images", source = "images", qualifiedByName = "mapFutsalImageToList")
   @Mapping(target = "registrationPhoto", source = "registrationPhoto", qualifiedByName = "mapRegistrationPhoto")
   @Mapping(target = "coverImage", source = "coverImage", qualifiedByName = "mapCoverImage")
   public abstract FutsalResponseDTO toDto(FutsalEntity futsal);

   @Named("mapFutsalImageToString")
   protected String mapFutsalImageToString(FutsalImages image) {
      if (image == null || image.getImageUrl() == null) {
         return null;
      }
      return baseUrl + image.getImageUrl();
   }

   @Named("mapFutsalImageToList")
   protected List<String> mapFutsalImageToList(List<FutsalImages> images) {
      if (images == null) {
         return Collections.emptyList();
      }
      return images.stream()
            .map(this::mapFutsalImageToString)
            .collect(Collectors.toList());
   }

   @Named("mapRegistrationPhoto")
   protected String mapRegistrationPhoto(String registrationPhoto) {
      if (registrationPhoto == null || registrationPhoto.isEmpty())
         return null;
      return baseUrl + registrationPhoto;
   }

   @Named(value = "mapCoverImage")
   protected String mapCoverImage(String coverImage) {
      if (coverImage == null || coverImage.isEmpty())
         return null;
      return baseUrl + coverImage;
   }

}
