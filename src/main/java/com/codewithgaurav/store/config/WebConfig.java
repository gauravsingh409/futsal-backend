package com.codewithgaurav.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// serve static file

@Configuration
public class WebConfig implements WebMvcConfigurer {
   /*
    * Implementing the WebMvcConfigurer interface means we are customizing
    * or overriding Spring Boot’s default MVC configuration.
    *
    * By default, Spring Boot provides some internal MVC configurations,
    * including static resource handling.
    *
    * When we override methods like addResourceHandlers, we can customize
    * how static files are served—such as adding new resource locations
    * or URL patterns for static content.
    */
   @Override
   public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
      String uploadDir = System.getProperty("user.dir") + "/uploads/";
      /*
       * addResourceHandler ->
       * Defines the URL pattern that Spring will treat as requests for static
       * resources.
       * It tells Spring to serve every file requested under "/uploads/**" as a static
       * file.
       */
      registry.addResourceHandler("/uploads/**")
            /*
             * addResourceLocations ->
             * Specifies the actual physical location of the static files on the server.
             * 'uploadDir' contains the full absolute path to the uploads folder in your
             * project directory,
             * e.g., E:/Gaurav/Futsal-BE/store/uploads
             *
             * Without this, Spring knows to serve URLs starting with "/uploads/",
             * but it doesn’t know where on the filesystem to find these files.
             * This location tells Spring exactly where to look for these files.
             */
            .addResourceLocations("file:" + uploadDir + "/")
            /*
             * setCachePeriod ->
             * Sets the amount of time (in seconds) that browsers should cache these static
             * resources.
             * Here, 3600 seconds means 1 hour caching to improve performance.
             */
            .setCachePeriod(3600);
   }
   
   
}