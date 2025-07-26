package com.codewithgaurav.store.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "futsal_images")
public class FutsalImages {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String imageUrl;

   @ManyToOne
   @JoinColumn(name = "futsal_id", nullable = false)
   private FutsalEntity futsal;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public FutsalEntity getFutsal() {
      return futsal;
   }

   public void setFutsal(FutsalEntity futsal) {
      this.futsal = futsal;
   }
   
   
   
}
