package com.codewithgaurav.store.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "futsal_owner")
public class FootsalModel {
   @Id
   private String id;
   private String username;
   private String password;
   private String citizenship_number;
   private String phone_no;

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

   public String getCitizenship_number() {
      return citizenship_number;
   }

   public void setCitizenship_number(String citizenship_number) {
      this.citizenship_number = citizenship_number;
   }

   public String getPhone_no() {
      return phone_no;
   }

   public void setPhone_no(String phone_no) {
      this.phone_no = phone_no;
   }
}
