package com.codewithgaurav.store.dto.request;

import com.codewithgaurav.store.validation.UserValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserRequestDto {

      // --- Common Fields for Login/Register ---
      @NotBlank(message = "Username is required", groups = {
                  UserValidation.UserRegisterGroup.class, UserValidation.UserLoginGroup.class,
                  UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      })
      @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters", groups = {
                  UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      })
      @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = {
                  UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      })
      private String username;

      @NotBlank(message = "Password is required", groups = {
                  UserValidation.UserRegisterGroup.class, UserValidation.UserLoginGroup.class,
                  UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      })
      // @Size(min = 6, message = "Password must be at least 6 characters", groups = {
      //             UserValidation.UserRegisterGroup.class, UserValidation.UserLoginGroup.class,
      //             UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      // })
      // @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password must contain uppercase, lowercase, number, and special character", groups = {
      //             UserValidation.OwnerRegisterGroup.class, UserValidation.OwnerLoginGroup.class
      // })
      private String password;

      // --- Fields for User Profile Completion ---
      @NotBlank(message = "Email is required", groups = { UserValidation.UserCompleteProfileGroup.class })
      @Email(message = "Invalid email format", groups = { UserValidation.UserCompleteProfileGroup.class })
      private String email;

      @NotBlank(message = "Address is required", groups = { UserValidation.UserCompleteProfileGroup.class })
      private String address; // Simple String for User address

      @NotBlank(message = "Phone number is required", groups = { UserValidation.UserCompleteProfileGroup.class })
      @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format", groups = {
                  UserValidation.UserCompleteProfileGroup.class })
      private String phone_no;

      private String profile_picture; // optional for User

      // --- Fields for Owner Registration/Profile Completion ---
      @NotBlank(message = "Citizenship number is required", groups = { UserValidation.OwnerRegisterGroup.class })
      @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Citizenship number can only contain letters, numbers, and hyphens", groups = {
                  UserValidation.OwnerRegisterGroup.class
      })
      private String citizenshipNumber;

      @NotBlank(message = "Phone number is required for owner", groups = { UserValidation.OwnerRegisterGroup.class })
      @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits for owner", groups = {
                  UserValidation.OwnerRegisterGroup.class
      })
      private String owner_phone_no;

      @NotBlank(message = "Fullname is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
      private String fullName;

      @NotNull(message = "Date of birth is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
      private LocalDate dateOfBirth;

      @NotBlank(message = "Emergency Contact is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
      private String emergencyContact;

      @NotBlank(message = "Profile picture URL is required", groups = {
                  UserValidation.OwnerProfileCompleteGroup.class })
      private String profileImageUrl;

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

}