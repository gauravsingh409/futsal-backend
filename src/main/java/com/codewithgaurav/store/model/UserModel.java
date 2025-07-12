package com.codewithgaurav.store.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "users") // Maps this class to a MongoDB collection named "users"
public class UserModel {

      @Id
      private String id;
      private String username;
      private String password;
      private String email;
      private String address; // Simple String for User address
      private String phone_no;
      private String profile_picture; // optional for User

      // Owner specific fields
      private String citizenshipNumber;
      private String owner_phone_no;
      private String fullName;
      private LocalDate dateOfBirth;
      private String emergencyContact;
      private String profileImageUrl;
      private boolean is_user = true;
      private boolean is_owner = false;

      public String getId() {
            return id;
      }

      public void setId(String id) {
            this.id = id;
      }

      public String getUsername() {
            return username;
      }

      public void setUsername(String username) {
            this.username = username;
      }

      public String getPassword() {
            return password;
      }

      public void setPassword(String password) {
            this.password = password;
      }

      public String getEmail() {
            return email;
      }

      public void setEmail(String email) {
            this.email = email;
      }

      public String getAddress() {
            return address;
      }

      public void setAddress(String address) {
            this.address = address;
      }

      public String getPhone_no() {
            return phone_no;
      }

      public void setPhone_no(String phone_no) {
            this.phone_no = phone_no;
      }

      public String getProfile_picture() {
            return profile_picture;
      }

      public void setProfile_picture(String profile_picture) {
            this.profile_picture = profile_picture;
      }

      public String getCitizenshipNumber() {
            return citizenshipNumber;
      }

      public void setCitizenshipNumber(String citizenshipNumber) {
            this.citizenshipNumber = citizenshipNumber;
      }

      public String getOwner_phone_no() {
            return owner_phone_no;
      }

      public void setOwner_phone_no(String owner_phone_no) {
            this.owner_phone_no = owner_phone_no;
      }

      public String getFullName() {
            return fullName;
      }

      public void setFullName(String fullName) {
            this.fullName = fullName;
      }

      public LocalDate getDateOfBirth() {
            return dateOfBirth;
      }

      public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
      }

      public String getEmergencyContact() {
            return emergencyContact;
      }

      public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
      }

      public String getProfileImageUrl() {
            return profileImageUrl;
      }

      public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
      }

      public boolean isIs_user() {
            return is_user;
      }

      public void setIs_user(boolean is_user) {
            this.is_user = is_user;
      }

      public boolean isIs_owner() {
            return is_owner;
      }

      public void setIs_owner(boolean is_owner) {
            this.is_owner = is_owner;
      }

}