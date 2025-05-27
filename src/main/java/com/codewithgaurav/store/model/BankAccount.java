package com.codewithgaurav.store.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class BankAccount {

   public enum AccountType {
      Savings,
      Current
   }

   @NotBlank(message = "Account holder name is required")
   private String accountHolderName;

   @NotBlank(message = "Account number is required")
   private String accountNumber;

   @NotBlank(message = "Bank name is required")
   private String bankName;

   @NotBlank(message = "Branch is required")
   private String branch;

   @NotNull(message = "Account type is required Saving or Current")
   private String accountType;

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

   @Override
   public String toString() {
      return "BankAccount{" +
            "accountHolderName='" + accountHolderName + '\'' +
            ", accountNumber='" + accountNumber + '\'' +
            ", bankName='" + bankName + '\'' +
            ", branch='" + branch + '\'' +
            ", accountType=" + accountType +
            '}';
   }

}
