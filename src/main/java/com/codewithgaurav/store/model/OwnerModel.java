package com.codewithgaurav.store.model;

import java.time.LocalDate;
import java.util.List;

import com.codewithgaurav.store.validation.UserValidation;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.Valid;

@Document(collection = "owner")
public class OwnerModel {
    @Id
    private String id;

    @NotBlank(message = "Username field is missing", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    private String username;


    @NotBlank(message = "Password field is missing", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password must contain uppercase, lowercase, number, and special character", groups = {UserValidation.OwnerLoginGroup.class, UserValidation.OwnerRegisterGroup.class})
    private String password;

    @NotBlank(message = "Citizenship number is required", groups = {UserValidation.OwnerRegisterGroup.class})
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Citizenship number can only contain letters, numbers, and hyphens", groups = {UserValidation.OwnerRegisterGroup.class})
    private String citizenship_number;


    @NotBlank(message = "Phone number is required", groups = {UserValidation.OwnerRegisterGroup.class})
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits", groups = {UserValidation.OwnerRegisterGroup.class})
    private String phone_no;

    // Additional Details
    @NotBlank(message = "Fullname is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private String fullName;

    @NotBlank(message = "Email is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private String email;

    @NotNull(message = "Address is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    @Valid
    private Address address; // Embedded document

    @NotNull(message = "Email is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private LocalDate dateOfBirth; // Using LocalDate for DOB

    @NotBlank(message = "Emergency Contact is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private String emergencyContact;

    @NotEmpty(message = "At least one bank account is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    @Valid
    private List<BankAccount> bankAccounts;

    @NotEmpty(message = "Profile picture is required", groups = {UserValidation.OwnerProfileCompleteGroup.class})
    private String profileImageUrl;

    private boolean is_user = false;
    private boolean is_owner = true;

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

    public String getFullName() {
        return fullName;
    }

    public String getCitizenship_number() {
        return citizenship_number;
    }

    public void setCitizenship_number(String citizenship_number) {
        this.citizenship_number = citizenship_number;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
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

    public boolean isIs_user() {
        return is_user;
    }

    public void setIs_user(boolean is_user) {
        this.is_user = is_user;
    }

    public boolean isIs_owner() {
        return is_owner;
    }

    public void setIs_owner(boolean is_owner) {
        this.is_owner = is_owner;
    }

    @Override
    public String toString() {
        return "OwnerModel{" + "id='" + id + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + ", citizenship_number='" + citizenship_number + '\'' + ", phone_no='" + phone_no + '\'' + ", fullName='" + fullName + '\'' + ", email='" + email + '\'' + ", address=" + address + ", dateOfBirth=" + dateOfBirth + ", emergencyContact='" + emergencyContact + '\'' + ", bankAccounts=" + bankAccounts + ", profileImageUrl='" + profileImageUrl + '\'' + ", is_user=" + is_user + ", is_owner=" + is_owner + '}';
    }
}
