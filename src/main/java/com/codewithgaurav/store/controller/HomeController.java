package com.codewithgaurav.store.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RequestMapping("/api")
@RestController
public class HomeController {

   @GetMapping("/")
   public Map<String, String> home() {
      Map<String, String> response = new HashMap<>();
      response.put("message", "Welcome to Gaurav Backend");
      response.put("Status", "success");
      return response;
   }

}
