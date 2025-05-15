package com.codewithgaurav.store.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")

public class UserModel {

   @Id
   private String id;
   private String username;
   private String password;

   // constructor
   public UserModel() {
   }

   public UserModel(String username, String password) {
      this.username = username;
      this.password = password;
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

}
