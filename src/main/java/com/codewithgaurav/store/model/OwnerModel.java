package com.codewithgaurav.store.model;

import java.time.LocalDate;
import java.util.List;

import com.codewithgaurav.store.validation.UserValidation;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Document(collection = "owner")
public class OwnerModel {
    @Id
    private String id;

    private String username;
    private String password;
    private String citizenshipNumber;
    private String phoneNo;

    // Additional Details
    @NotBlank(message = "Fullname is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
    private String fullName;

    @NotBlank(message = "Email is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
    private String email;

    @NotNull(message = "Address is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
    @Valid
    private Address address; // Embedded document

    @NotNull(message = "Email is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
    private LocalDate dateOfBirth; // Using LocalDate for DOB

    @NotBlank(message = "Emergency Contact is required", groups = { UserValidation.OwnerProfileCompleteGroup.class })
    private String emergencyContact;

    @NotEmpty(message = "At least one bank account is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    @Valid
    private List<BankAccount> bankAccounts;

    @NotEmpty(message = "Profile picture is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private String profileImageUrl;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCitizenship_number() {
        return citizenshipNumber;
    }

    public void setCitizenship_number(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getphoneNo() {
        return phoneNo;
    }

    public void setphoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    @Override
    public String toString() {
        return "OwnerModel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", citizenshipNumber='" + citizenshipNumber + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", dateOfBirth=" + dateOfBirth +
                ", emergencyContact='" + emergencyContact + '\'' +
                ", bankAccounts=" + bankAccounts +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                '}';
    }
}
