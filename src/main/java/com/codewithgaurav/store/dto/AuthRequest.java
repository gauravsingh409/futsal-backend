package com.codewithgaurav.store.dto;

import com.codewithgaurav.store.validation.LoginGroup;
import com.codewithgaurav.store.validation.RegisterGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthRequest {

   @NotBlank(message = "Username field is missing", groups = { LoginGroup.class, RegisterGroup.class })
   @Size(min = 5, message = "Username must be at least 5 characters", groups = { LoginGroup.class,
         RegisterGroup.class })
   private String username;

   @NotBlank(message = "Password field is missing", groups = { LoginGroup.class, RegisterGroup.class })
   @Size(min = 5, message = "Password must be at least 5 characters", groups = { LoginGroup.class,
         RegisterGroup.class })
   private String password;

   // Getters and Setters
   public String getUsername() {

      return username;
   }

   public void setUsername(String username) {
      this.username = username.toLowerCase();
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

}
