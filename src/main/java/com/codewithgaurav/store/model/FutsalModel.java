package com.codewithgaurav.store.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.codewithgaurav.store.validation.FutsalValidation;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;
import java.util.List;

@Document(collection = "futsals")
public class FutsalModel {

    @Id
    private String id;

    @Field("name")
    private String name;

    @NotBlank(message = "Citizenship number is required", groups = {
            FutsalValidation.FutsalRegister.class })
    @Field("registration_number")
    @Indexed(unique = true)
    private String registrationNumber;

    @Field("location")
    private GeoJsonPoint location;

    @Field("district")
    private String district;

    @Field("city")
    private String city;

    @Field("images")
    private List<String> images;

    @Field("cover_image")
    private String converImage;

    @Field("registration_photo")
    private String registrationPhoto;

    @Field("owner")
    @DBRef
    private OwnerModel owner;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
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

    public GeoJsonPoint getLocation() {
        return location;
    }

    public void setLocation(GeoJsonPoint location) {
        this.location = location;
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

    public OwnerModel getOwner() {
        return owner;
    }

    public void setOwner(OwnerModel owner) {
        this.owner = owner;
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

    public String getRegistrationPhoto() {
        return registrationPhoto;
    }

    public void setRegistrationPhoto(String registrationPhoto) {
        this.registrationPhoto = registrationPhoto;
    }

    @Override
    public String toString() {
        return "FutsalModel [id=" + id + ", name=" + name + ", registrationNumber=" + registrationNumber + ", location="
                + location + ", district=" + district + ", city=" + city
                + ", images=" + images + ", converImage=" + converImage + ", registrationPhoto=" + registrationPhoto
                + ", owner=" + owner + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
    }

}
