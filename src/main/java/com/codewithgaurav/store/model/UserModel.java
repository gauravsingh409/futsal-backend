package com.codewithgaurav.store.model;

import com.codewithgaurav.store.validation.UserValidation;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")

public class UserModel {

   @Id
   private String id;

   @NotBlank(message = "Username is required", groups = { UserValidation.UserRegisterGroup.class,
         UserValidation.UserLoginGroup.class })
   private String username;

   @NotBlank(message = "Password is required", groups = { UserValidation.UserLoginGroup.class,
         UserValidation.UserRegisterGroup.class })
   @Size(min = 6, message = "Password must be at least 6 characters")
   private String password;

   @NotBlank(message = "Email is required", groups = { UserValidation.UserCompleteProfileGroup.class })
   @Email(message = "Invalid email format", groups = { UserValidation.UserCompleteProfileGroup.class })
   private String email;

   private String profile_picture; // optional, no validation

   @NotBlank(message = "Address is required", groups = { UserValidation.UserCompleteProfileGroup.class })
   private String address;

   private boolean is_user = true;
   private boolean is_owner = false;

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

   @NotBlank(message = "Phone number is required", groups = { UserValidation.UserCompleteProfileGroup.class })
   @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Invalid phone number format")
   private String phone_no;

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getProfile_picture() {
      return profile_picture;
   }

   public void setProfile_picture(String profile_picture) {
      this.profile_picture = profile_picture;
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

   public boolean isVerified() {
      return isVerified;
   }

   public void setVerified(boolean verified) {
      isVerified = verified;
   }

   private boolean isVerified;

   // constructor
   public UserModel() {
   }

   // Getters and Setters
   public String getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public String getPassword() {
      return password;
   }

   public void setId(String id) {
      this.id = id;
   }

   public void setUsername(String username) {
      this.username = username.toLowerCase();
   }

   public void setPassword(String password) {
      this.password = password;
   }

   @Override
   public String toString() {
      return "UserModel{" +
            "id='" + id + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", profile_picture='" + profile_picture + '\'' +
            ", address='" + address + '\'' +
            ", is_user=" + is_user +
            ", is_owner=" + is_owner +
            ", phone_no='" + phone_no + '\'' +
            ", isVerified=" + isVerified +
            '}';
   }
}
