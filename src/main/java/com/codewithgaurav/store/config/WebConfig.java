package com.codewithgaurav.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Override
   public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
      String uploadDir = System.getProperty("user.dir") + "/uploads/";
      registry.addResourceHandler("/uploads/**") // spring handle this as static file
            .addResourceLocations("file:" + uploadDir + "/")
            .setCachePeriod(3600);
   }
}