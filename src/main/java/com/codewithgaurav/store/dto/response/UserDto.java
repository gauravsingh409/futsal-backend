package com.codewithgaurav.store.dto.response;

import java.time.LocalDate;

public class UserDto {
   private String id;
   private String username;
   private String citizenshipNumber;
   private String phone_no;
   private String fullName;
   private String email;
   private LocalDate dateOfBirth;
   private String profileImageUrl;
   private boolean isUser = false;
   private boolean isOwner = true;
   private boolean isAdmin = false;

   // Getter & Setter
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

   public String getCitizenshipNumber() {
      return citizenshipNumber;
   }

   public void setCitizenshipNumber(String citizenshipNumber) {
      this.citizenshipNumber = citizenshipNumber;
   }

   public String getPhone_no() {
      return phone_no;
   }

   public void setPhone_no(String phone_no) {
      this.phone_no = phone_no;
   }

   public String getFullName() {
      return fullName;
   }

   public void setFullName(String fullName) {
      this.fullName = fullName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public LocalDate getDateOfBirth() {
      return dateOfBirth;
   }

   public void setDateOfBirth(LocalDate dateOfBirth) {
      this.dateOfBirth = dateOfBirth;
   }

   public String getProfileImageUrl() {
      return profileImageUrl;
   }

   public void setProfileImageUrl(String profileImageUrl) {
      this.profileImageUrl = profileImageUrl;
   }

   public boolean isOwner() {
      return isOwner;
   }

   public void setOwner(boolean isOwner) {
      this.isOwner = isOwner;
   }

   public boolean isUser() {
      return isUser;
   }

   public void setUser(boolean isUser) {
      this.isUser = isUser;
   }

   public void setAdmin(boolean isAdmin) {
      this.isAdmin = isAdmin;
   }

   public boolean isAdmin() {
      return isAdmin;
   }
}
