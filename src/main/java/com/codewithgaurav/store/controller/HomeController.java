package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithgaurav.store.response.ApiResponse;

@RestController
public class HomeController {

   // @GetMapping("/")
   // public Map<String, String> home() {
   // Map<String, String> response = new HashMap<>();
   // response.put("message", "Welcome to Gaurav Backend");
   // response.put("Status", "success");
   // return response;
   // }

   @GetMapping("/")
   public ApiResponse home() {

      Map<String, String> response = new HashMap<>();
      response.put("message", "Welcome to Gaurav Backend");
      response.put("Status", "success");

      return new ApiResponse("Welcome to the Spring Boot Backend", true, response);
   }

}
