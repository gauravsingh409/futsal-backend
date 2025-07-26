package com.codewithgaurav.store.dto.response;

import java.util.List;

public class FutsalResponseDTO {
   private Long id;
   private String name;
   private String city;
   private String district;
   private String registrationNumber;
   private String registrationPhoto;
   private String coverImage;
   private Double latitude;
   private Double longitude;
   private List<String> imageUrls;

   // Getters and setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getDistrict() {
      return district;
   }

   public void setDistrict(String district) {
      this.district = district;
   }

   public String getRegistrationNumber() {
      return registrationNumber;
   }

   public void setRegistrationNumber(String registrationNumber) {
      this.registrationNumber = registrationNumber;
   }

   public String getRegistrationPhoto() {
      return registrationPhoto;
   }

   public void setRegistrationPhoto(String registrationPhoto) {
      this.registrationPhoto = registrationPhoto;
   }

   public String getCoverImage() {
      return coverImage;
   }

   public void setCoverImage(String coverImage) {
      this.coverImage = coverImage;
   }

   public Double getLatitude() {
      return latitude;
   }

   public void setLatitude(Double latitude) {
      this.latitude = latitude;
   }

   public Double getLongitude() {
      return longitude;
   }

   public void setLongitude(Double longitude) {
      this.longitude = longitude;
   }

   public List<String> getImageUrls() {
      return imageUrls;
   }

   public void setImageUrls(List<String> imageUrls) {
      this.imageUrls = imageUrls;
   }
}
