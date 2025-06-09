package com.codewithgaurav.store.payload;

import java.util.List;

public class PaginatedResponse<T> {
   private List<T> data;
   private int page;
   private int pageSize;
   private long totalItems;
   private int totalPages;

   // Getter and Setter
   public List<T> getData() {
      return data;
   }

   public void setData(List<T> data) {
      this.data = data;
   }

   public int getPage() {
      return page;
   }

   public void setPage(int page) {
      this.page = page;
   }

   public int getPageSize() {
      return pageSize;
   }

   public void setPageSize(int pageSize) {
      this.pageSize = pageSize;
   }

   public int getTotalPages() {
      return totalPages;
   }

   public void setTotalPages(int totalPages) {
      this.totalPages = totalPages;
   }

   public long getTotalItems() {
      return totalItems;
   }

   public void setTotalItems(long totalItems) {
      this.totalItems = totalItems;
   }

   public PaginatedResponse(List<T> data, int page, int pageSize, long totalItems, int totalPages) {
      this.data = data;
      this.page = page;
      this.pageSize = pageSize;
      this.totalItems = totalItems;
      this.totalPages = totalPages;
   }

}
