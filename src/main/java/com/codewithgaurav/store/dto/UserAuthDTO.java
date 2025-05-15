package com.codewithgaurav.store.dto;

import com.codewithgaurav.store.validation.UserLoginGroup;
import com.codewithgaurav.store.validation.UserRegisterGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserAuthDTO {

   @NotBlank(message = "Username field is missing", groups = { UserLoginGroup.class, UserRegisterGroup.class })
   @Size(min = 5, message = "Username must be at least 5 characters", groups = { UserLoginGroup.class,
         UserRegisterGroup.class })
   private String username;

   @NotBlank(message = "Password field is missing", groups = { UserLoginGroup.class, UserRegisterGroup.class })
   @Size(min = 5, message = "Password must be at least 5 characters", groups = { UserLoginGroup.class,
         UserRegisterGroup.class })
   private String password;


   // Getter and setter here
   public String getUsername() {
      return username;
   }

   @Override
   public String toString() {
      return "UserAuthDTO{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
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
}
