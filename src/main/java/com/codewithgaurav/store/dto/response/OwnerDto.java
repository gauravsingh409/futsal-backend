package com.codewithgaurav.store.dto.response;

import java.time.LocalDate;

public class OwnerDto {
   private String id;
   private String username;
   private String citizenshipNumber;
   private String phone_no;
   private String fullName;
   private String email;
   private Address address;
   private LocalDate dateOfBirth;
   private String emergencyContact;
   private String profileImageUrl;
   private boolean is_user = false;
   private boolean is_owner = true;

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

   public Address getAddress() {
      return address;
   }

   public void setAddress(Address address) {
      this.address = address;
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
   // Getter & Setter

   public static class Address {
      private String street;
      private String city;
      private String province;
      private String country;
      private String postalCode;

      // Getter & Setter
      public String getStreet() {
         return street;
      }

      public void setStreet(String street) {
         this.street = street;
      }

      public String getCity() {
         return city;
      }

      public void setCity(String city) {
         this.city = city;
      }

      public String getProvince() {
         return province;
      }

      public void setProvince(String province) {
         this.province = province;
      }

      public String getCountry() {
         return country;
      }

      public void setCountry(String country) {
         this.country = country;
      }

      public String getPostalCode() {
         return postalCode;
      }

      public void setPostalCode(String postalCode) {
         this.postalCode = postalCode;
      }
      // Getter & Setter
   }
}
