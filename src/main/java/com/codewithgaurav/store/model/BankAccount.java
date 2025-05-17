package com.codewithgaurav.store.model;

public class BankAccount {
   private String accountHolderName;
   private String accountNumber;
   private String bankName;
   private String branch;
   private String accountType; // e.g., "Savings", "Current"

   // Getter for accountHolderName
   public String getAccountHolderName() {
      return accountHolderName;
   }

   // Setter for accountHolderName
   public void setAccountHolderName(String accountHolderName) {
      this.accountHolderName = accountHolderName;
   }

   // Getter for accountNumber
   public String getAccountNumber() {
      return accountNumber;
   }

   // Setter for accountNumber
   public void setAccountNumber(String accountNumber) {
      this.accountNumber = accountNumber;
   }

   // Getter for bankName
   public String getBankName() {
      return bankName;
   }

   // Setter for bankName
   public void setBankName(String bankName) {
      this.bankName = bankName;
   }

   // Getter for branch
   public String getBranch() {
      return branch;
   }

   // Setter for branch
   public void setBranch(String branch) {
      this.branch = branch;
   }

   // Getter for accountType
   public String getAccountType() {
      return accountType;
   }

   // Setter for accountType
   public void setAccountType(String accountType) {
      this.accountType = accountType;
   }

}
