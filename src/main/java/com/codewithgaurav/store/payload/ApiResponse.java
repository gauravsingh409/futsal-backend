package com.codewithgaurav.store.payload;

public class ApiResponse<T> {
   private String message;
   private int code;
   private String success;
   private T data;
   private Object errors;

   public ApiResponse() {
   }

   public ApiResponse(String message, int code, String success, T data, Object errors) {
      this.message = message;
      this.code = code;
      this.success = success;
      this.data = data;
      this.errors = errors;
   }

   public ApiResponse(String message, int code, String success, T data) {
      this.message = message;
      this.code = code;
      this.success = success;
      this.data = data;
   }

   public ApiResponse(String message, int code, String success) {
      this.message = message;
      this.code = code;
      this.success = success;
   }

   // Getters and setters
   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public int getCode() {
      return code;
   }

   public void setCode(int code) {
      this.code = code;
   }

   public T getData() {
      return data;
   }

   public void setData(T data) {
      this.data = data;
   }

   public Object getErrors() {
      return errors;
   }

   public void setErrors(Object errors) {
      this.errors = errors;
   }

   public String getSuccess() {
      return success;
   }

   public void setSuccess(String success) {
      this.success = success;
   }
}
