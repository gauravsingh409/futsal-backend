package com.codewithgaurav.store.validator;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String> {
   // private final List<String> allowedRoles = List.of("owner", "user", "admin");
   private List<String> allowedRoles;

   @Override
   public void initialize(ValidRole constraintAnnotation) {
      String[] values = constraintAnnotation.allowed();
      this.allowedRoles = (values.length == 0) ? List.of("user", "admin")
            : Arrays.stream(constraintAnnotation.allowed()).map(String::toLowerCase).toList();
   }

   @Override
   public boolean isValid(String value, ConstraintValidatorContext context) {
      if (value == null)
         return true;
      return allowedRoles.contains(value.toLowerCase());
   }
}
