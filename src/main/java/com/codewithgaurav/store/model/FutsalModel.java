package com.codewithgaurav.store.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;
import java.util.List;
@Document(collection = "futsals")
public class FutsalModel {

    @Id
    private String id;

    @Field("name")
    private String futsal_name;

    @Field("regisgration_number")
    private String futsal_registration_number;

    @Field("address")
    private String futsal_address;

    @Field("location")
    private String futsal_location;

    @Field("state")
    private String futsal_state;

    @Field("district")
    private String futsal_district;

    @Field("city")
    private String futsal_city;

    @Field("images")
    private List<String> futsal_images;

    @Field("cover_image")
    private String futsal_cover_images;

    @Field("owner")
    private String owner_id;

    @CreatedDate
    private Date created_at;

    @LastModifiedDate
    private Date updated_at;

    public String getFutsal_address() {
        return futsal_address;
    }

    public void setFutsal_address(String futsal_address) {
        this.futsal_address = futsal_address;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getFutsal_cover_images() {
        return futsal_cover_images;
    }

    public void setFutsal_cover_images(String futsal_cover_images) {
        this.futsal_cover_images = futsal_cover_images;
    }

    public List getFutssal_images() {
        return futsal_images;
    }

    public void setFutssal_images(List futssal_images) {
        this.futsal_images = futssal_images;
    }

    public String getFutsal_city() {
        return futsal_city;
    }

    public void setFutsal_city(String futsal_city) {
        this.futsal_city = futsal_city;
    }

    public String getFutsal_district() {
        return futsal_district;
    }

    public void setFutsal_district(String futsal_district) {
        this.futsal_district = futsal_district;
    }

    public String getFutsal_location() {
        return futsal_location;
    }

    public void setFutsal_location(String futsal_location) {
        this.futsal_location = futsal_location;
    }

    public String getFutsal_state() {
        return futsal_state;
    }

    public void setFutsal_state(String futsal_state) {
        this.futsal_state = futsal_state;
    }

    public String getFutsal_registration_number() {
        return futsal_registration_number;
    }

    public void setFutsal_registration_number(String futsal_registration_number) {
        this.futsal_registration_number = futsal_registration_number;
    }

    public String getFutsal_name() {
        return futsal_name;
    }

    public void setFutsal_name(String futsal_name) {
        this.futsal_name = futsal_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFutsal_images() {
        return futsal_images;
    }

    public void setFutsal_images(List<String> futsal_images) {
        this.futsal_images = futsal_images;
    }


    @Override
    public String toString() {
        return "FutsalModel{" +
                "id='" + id + '\'' +
                ", futsal_name='" + futsal_name + '\'' +
                ", futsal_registration_number='" + futsal_registration_number + '\'' +
                ", futsal_address='" + futsal_address + '\'' +
                ", futsal_location='" + futsal_location + '\'' +
                ", futsal_state='" + futsal_state + '\'' +
                ", futsal_district='" + futsal_district + '\'' +
                ", futsal_city='" + futsal_city + '\'' +
                ", futsal_images=" + futsal_images +
                ", futsal_cover_images='" + futsal_cover_images + '\'' +
                ", owner_id='" + owner_id + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
