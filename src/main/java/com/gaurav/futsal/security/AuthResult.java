package com.gaurav.futsal.security;

public class AuthResult {
   private boolean valid;
   private Long userId;
   private String errorMessage;

   public boolean isValid() {
      return valid;
   }

   public void setValid(boolean valid) {
      this.valid = valid;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public String getErrorMessage() {
      return errorMessage;
   }

   public void setErrorMessage(String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public AuthResult(boolean valid, Long userId, String errorMessage) {
      this.valid = valid;
      this.userId = userId;
      this.errorMessage = errorMessage;
   }

}
