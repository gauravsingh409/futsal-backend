package com.codewithgaurav.store.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class UserEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false, unique = true)
   private String username;
   private String password;
   private String email;
   private String address;
   private String phoneNo;
   private String profilePicture;
   // Owner-specific fields
   private String citizenshipNumber;
   private String fullName;
   private LocalDate dateOfBirth;
   private boolean isUser = true;
   private boolean isOwner = false;
   private boolean isAdmin = false;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
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

   public String getPhoneNo() {
      return phoneNo;
   }

   public void setPhoneNo(String phoneNo) {
      this.phoneNo = phoneNo;
   }

   public String getProfilePicture() {
      return profilePicture;
   }

   public void setProfilePicture(String profilePicture) {
      this.profilePicture = profilePicture;
   }

   public String getCitizenshipNumber() {
      return citizenshipNumber;
   }

   public void setCitizenshipNumber(String citizenshipNumber) {
      this.citizenshipNumber = citizenshipNumber;
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

   public boolean isUser() {
      return isUser;
   }

   public void setUser(boolean isUser) {
      this.isUser = isUser;
   }

   public boolean isOwner() {
      return isOwner;
   }

   public void setOwner(boolean isOwner) {
      this.isOwner = isOwner;
   }

   public boolean isAdmin() {
      return isAdmin;
   }

   public void setAdmin(boolean isAdmin) {
      this.isAdmin = isAdmin;
   }

}
