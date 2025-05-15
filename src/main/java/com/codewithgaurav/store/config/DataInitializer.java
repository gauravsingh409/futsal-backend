package com.codewithgaurav.store.config;

import com.codewithgaurav.store.model.FootsalLocationModel;
import com.codewithgaurav.store.repository.FootsalLocationRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

   private final FootsalLocationRepository repository;

   public DataInitializer(FootsalLocationRepository repository) {
      this.repository = repository;
   }

   @PostConstruct
   public void init() {
      repository.deleteAll();

      // Add sample futsal locations (latitude, longitude)
      repository.save(new FootsalLocationModel("Futsal Arena", "Kathmandu", 27.7172, 85.3240));
      repository.save(new FootsalLocationModel("Goal Masters", "Lalitpur", 27.6667, 85.3167));
      repository.save(new FootsalLocationModel("Soccer Zone", "Bhaktapur", 27.6710, 85.4295));
      repository.save(new FootsalLocationModel("Footy Palace", "Patan", 27.6766, 85.3149));
   }
}