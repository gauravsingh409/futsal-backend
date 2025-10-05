package com.gaurav.futsal.exception;

import com.gaurav.futsal.payload.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ValidationHandler {

   /*
    * the annotation tells, if any controller throws specified error like
    * MethodArgumentNotValidException anywhere
    * in a controller, handle it with this method
    * MethodArgumentNotValidException is thrown by spring if we
    * use @Valid, @Validated on @RequestBody
    */
   @ExceptionHandler(MethodArgumentNotValidException.class)
   public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
      /*
       * Map define the structure (key-value) pairs
       * HashMap is one of storing and organizing those key-value pairs efficiently
       * using hashing
       */

      Map<String, List<String>> errors = new HashMap<>();

      /*
       * Advanced For loop or for each loop
       * for(DataType variable : collection)
       * eg:- String[] fruits = { "Apple", "Banana", "Mango" };
       * 
       * for (String fruit : fruits) {
       * System.out.println(fruit);
       * }
       */
      for (FieldError error : ex.getBindingResult().getFieldErrors()) {
         /*
          * computeIfAbsent checks if the key is already present in the map.
          * If the key (i.e., the field name which is returned by error.getField() is
          * present, it returns the existing value
          * (the associated list of error messages).
          * If the key is not present, it creates a new value using the provided lambda
          * expression and adds it to the map.
          * In this case, the key is error.getField() (which returns the field name from
          * FieldError),
          * and the value is a new ArrayList (which will hold the error messages for that
          * field).
          */

         errors.computeIfAbsent(error.getField(), key -> new ArrayList<>()).add(error.getDefaultMessage());
      }
      ApiResponse<Object> response = new ApiResponse<>(
            "Validation failed",
            400,
            false,
            new HashMap<>(),
            errors);

      return ResponseEntity.badRequest().body(response);
   }
}
