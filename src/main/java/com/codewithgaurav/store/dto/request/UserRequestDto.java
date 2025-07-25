package com.codewithgaurav.store.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

import com.codewithgaurav.store.validation.UserValidation;

public class UserRequestDto {

      private Long id;

      @NotBlank(message = "Username is required", groups = {
                  UserValidation.UserRegisterGroup.class,
                  UserValidation.OwnerRegisterGroup.class
      })
      @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters", groups = {
                  UserValidation.UserRegisterGroup.class,
                  UserValidation.OwnerRegisterGroup.class
      })
      @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = {
                  UserValidation.UserRegisterGroup.class,
                  UserValidation.OwnerRegisterGroup.class
      })
      private String username;

      // @NotBlank(message = "Password is required", groups = {
      // UserValidation.UserRegisterGroup.class,
      // UserValidation.OwnerRegisterGroup.class
      // })
      // @Size(min = 6, message = "Password must be at least 6 characters", groups = {
      // UserValidation.UserRegisterGroup.class,
      // UserValidation.OwnerRegisterGroup.class
      // })
      private String password;

      @NotBlank(message = "Email is required", groups = {
                  UserValidation.UserCompleteProfileGroup.class,
                  UserValidation.UserCompleteProfileGroup.class
      })
      @Email(message = "Invalid email format", groups = {
                  UserValidation.UserCompleteProfileGroup.class,
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private String email;

      @NotBlank(message = "Address is required", groups = {
                  UserValidation.UserCompleteProfileGroup.class,
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private String address;

      @NotBlank(message = "Phone number is required", groups = {
                  UserValidation.UserCompleteProfileGroup.class,
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format", groups = {
                  UserValidation.UserCompleteProfileGroup.class,
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private String phoneNo;

      private String profilePicture; // optional

      // Owner-specific fields
      @NotBlank(message = "Citizenship number is required", groups = {
                  UserValidation.OwnerRegisterGroup.class
      })
      @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Citizenship number can only contain letters, numbers, and hyphens", groups = {
                  UserValidation.OwnerRegisterGroup.class
      })
      private String citizenshipNumber;

      @NotBlank(message = "Full name is required", groups = {
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private String fullName;

      @NotNull(message = "Date of birth is required", groups = {
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      @Past(message = "Date of birth must be in the past", groups = {
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private LocalDate dateOfBirth;

      @NotBlank(message = "Profile image URL is required", groups = {
                  UserValidation.OwnerProfileCompleteGroup.class
      })
      private String profileImageUrl;

      // Boolean fields typically don't need validation
      private boolean isUser;
      private boolean isOwner;
      private boolean isAdmin;

      // Getters and Setters
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

      public String getProfileImageUrl() {
            return profileImageUrl;
      }

      public void setProfileImageUrl(String profileImageUrl) {
            this.profileImageUrl = profileImageUrl;
      }

      public boolean isUser() {
            return isUser;
      }

      public void setUser(boolean user) {
            isUser = user;
      }

      public boolean isOwner() {
            return isOwner;
      }

      public void setOwner(boolean owner) {
            isOwner = owner;
      }

      public boolean isAdmin() {
            return isAdmin;
      }

      public void setAdmin(boolean admin) {
            isAdmin = admin;
      }
}