package com.codewithgaurav.store.exception.customException;

public class NotAuthenticatedException extends RuntimeException {
   public NotAuthenticatedException(String message) {
      super(message);
   }
}
