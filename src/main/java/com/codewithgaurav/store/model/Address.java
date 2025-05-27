package com.codewithgaurav.store.model;

import jakarta.validation.constraints.NotBlank;

public class Address {

   @NotBlank(message = "Street is required")
   private String street;

   @NotBlank(message = "City is required")
   private String city;

   @NotBlank(message = "Province is required")
   private String province;

   @NotBlank(message = "Country is required")
   private String country;

   @NotBlank(message = "Postal code is required")
   private String postalCode;

   // Getter for street
   public String getStreet() {
      return street;
   }

   // Setter for street
   public void setStreet(String street) {
      this.street = street;
   }

   // Getter for city
   public String getCity() {
      return city;
   }

   // Setter for city
   public void setCity(String city) {
      this.city = city;
   }

   // Getter for province
   public String getProvince() {
      return province;
   }

   // Setter for province
   public void setProvince(String province) {
      this.province = province;
   }

   // Getter for country
   public String getCountry() {
      return country;
   }

   // Setter for country
   public void setCountry(String country) {
      this.country = country;
   }

   // Getter for postalCode
   public String getPostalCode() {
      return postalCode;
   }

   // Setter for postalCode
   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   @Override
   public String toString() {
      return "Address{" +
            "street='" + street + '\'' +
            ", city='" + city + '\'' +
            ", province='" + province + '\'' +
            ", country='" + country + '\'' +
            ", postalCode='" + postalCode + '\'' +
            '}';
   }
}
