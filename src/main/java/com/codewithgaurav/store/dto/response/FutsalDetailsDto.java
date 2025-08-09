package com.codewithgaurav.store.dto.response;

import java.util.Date;
import java.util.List;

public class FutsalDetailsDto {
   private String id;
   private String name;
   private String registrationNumber;
   private String district;
   private String city;
   private List<String> images;
   private String converImage;
   private String registrationPhoto;
   private UserDto user;
   private Date createdAt;
   private Date updatedAt;

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getRegistrationNumber() {
      return registrationNumber;
   }

   public void setRegistrationNumber(String registrationNumber) {
      this.registrationNumber = registrationNumber;
   }

   public String getDistrict() {
      return district;
   }

   public void setDistrict(String district) {
      this.district = district;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public List<String> getImages() {
      return images;
   }

   public void setImages(List<String> images) {
      this.images = images;
   }

   public String getConverImage() {
      return converImage;
   }

   public void setConverImage(String converImage) {
      this.converImage = converImage;
   }

   public String getRegistrationPhoto() {
      return registrationPhoto;
   }

   public void setRegistrationPhoto(String registrationPhoto) {
      this.registrationPhoto = registrationPhoto;
   }

   public UserDto getUser() {
      return user;
   }

   public void setUser(UserDto user) {
      this.user = user;
   }

   public Date getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
   }

   public Date getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
   }

   @Override
   public String toString() {
      return "FutsalDetailsDto [id=" + id + ", name=" + name + ", registrationNumber=" + registrationNumber
            + ", district=" + district + ", city=" + city + ", images=" + images
            + ", converImage=" + converImage + ", registrationPhoto=" + registrationPhoto + ", user=" + user
            + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
   }

}
