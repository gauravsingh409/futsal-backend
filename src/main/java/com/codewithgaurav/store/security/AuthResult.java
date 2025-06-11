package com.codewithgaurav.store.security;

public class AuthResult {
   private boolean valid;
   private String userId;
   private String errorMessage;

   public boolean isValid() {
      return valid;
   }

   public void setValid(boolean valid) {
      this.valid = valid;
   }

   public String getUserId() {
      return userId;
   }

   public void setUserId(String userId) {
      this.userId = userId;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public AuthResult(boolean valid, String userId, String errorMessage) {
      this.valid = valid;
      this.userId = userId;
      this.errorMessage = errorMessage;
   }

}
