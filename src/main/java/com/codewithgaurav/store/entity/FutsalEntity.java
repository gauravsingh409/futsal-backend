package com.codewithgaurav.store.entity;

import com.codewithgaurav.store.validation.FutsalValidation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "futsals")
public class FutsalEntity {

      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;

      @Column(name = "name", nullable = false)
      private String name;

      @NotBlank(message = "Citizenship number is required", groups = {
                  FutsalValidation.FutsalRegister.class })
      @Column(name = "registration_number", nullable = false, unique = true)
      private String registrationNumber;
      private Double latitude;
      private Double longitude;
      private String district;

      @Column(name = "city")
      private String city;

      // For storing multiple images, you might use a separate table with @OneToMany
      @ElementCollection
      @CollectionTable(name = "futsal_images", joinColumns = @JoinColumn(name = "futsal_id"))
      @Column(name = "image_url")
      private List<String> images;

      @Column(name = "cover_image")
      private String coverImage;

      @Column(name = "registration_photo")
      private String registrationPhoto;

      // Owner relationship with validation
      @ManyToOne
      @JoinColumn(name = "owner_id", nullable = false)
      private UserEntity user;

      @CreationTimestamp
      @Column(name = "created_at", updatable = false)
      private Date createdAt;

      @UpdateTimestamp
      @Column(name = "updated_at")
      private Date updatedAt;

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

      public String getRegistrationNumber() {
            return registrationNumber;
      }

      public void setRegistrationNumber(String registrationNumber) {
            this.registrationNumber = registrationNumber;
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

      public String getCoverImage() {
            return coverImage;
      }

      public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
      }

      public String getRegistrationPhoto() {
            return registrationPhoto;
      }

      public void setRegistrationPhoto(String registrationPhoto) {
            this.registrationPhoto = registrationPhoto;
      }

      public UserEntity getUser() {
            return user;
      }

      public void setUser(UserEntity user) {
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

}