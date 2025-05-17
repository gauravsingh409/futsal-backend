package com.codewithgaurav.store.model;

public class Address {
   private String street;
   private String city;
   private String province;
   private String country;
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
}
