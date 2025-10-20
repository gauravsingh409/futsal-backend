package com.gaurav.futsal.dto.request;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import com.gaurav.futsal.validation.UserValidation;
import lombok.Data;

@Data
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

}