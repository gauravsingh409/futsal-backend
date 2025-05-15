package com.codewithgaurav.store.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "captain")
public class FootsalModel {
   @Id
   private String id;
   private String username;
   private String password;
   private String footsal_name;
   private String registration_number;
   private String price_per_hour;
   private String contact_info;
   private String image;

   // Getters and Setters
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

   public String getFootsal_name() {
      return footsal_name;
   }

   public void setFootsal_name(String footsal_name) {
      this.footsal_name = footsal_name;
   }

   public String getRegistration_number() {
      return registration_number;
   }

   public void setRegistration_number(String registration_number) {
      this.registration_number = registration_number;
   }

   public String getPrice_per_hour() {
      return price_per_hour;
   }

   public void setPrice_per_hour(String price_per_hour) {
      this.price_per_hour = price_per_hour;
   }

   public String getContact_info() {
      return contact_info;
   }

   public void setContact_info(String contact_info) {
      this.contact_info = contact_info;
   }

   public String getImage() {
      return image;
   }

   public void setImage(String image) {
      this.image = image;
   }
}
