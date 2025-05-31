package com.codewithgaurav.store.dto;

import com.codewithgaurav.store.validation.UserValidation;

import jakarta.validation.constraints.*;

public class FutsalDto {

    // username
    @NotBlank(message = "Username field is missing", groups = { UserValidation.FutsalLoginGroup.class, UserValidation.FutsalRegisterGroup.class })
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters", groups = {
            UserValidation.FutsalLoginGroup.class,
            UserValidation.FutsalRegisterGroup.class })
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = {
            UserValidation.FutsalLoginGroup.class, UserValidation.FutsalRegisterGroup.class })
    private String username;

    // password
    @NotBlank(message = "Password field is missing", groups = {
            UserValidation.FutsalLoginGroup.class,
            UserValidation.FutsalRegisterGroup.class })
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = {
            UserValidation.FutsalLoginGroup.class,
            UserValidation.FutsalRegisterGroup.class })
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password must contain uppercase, lowercase, number, and special character", groups = {
            UserValidation.FutsalLoginGroup.class, UserValidation.FutsalRegisterGroup.class })
    private String password;

    // citizenship number
    @NotBlank(message = "Citizenship number is required", groups = {
            UserValidation.FutsalRegisterGroup.class })
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Citizenship number can only contain letters, numbers, and hyphens", groups = {
            UserValidation.FutsalRegisterGroup.class })
    private String citizenshipNumber;

    // phone number
    @NotBlank(message = "Phone number is required", groups = {
            UserValidation.FutsalRegisterGroup.class })
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits", groups = {
            UserValidation.FutsalRegisterGroup.class })
    private String phone_no;

    // Getters and Setters
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

    public String getCitizenshipNumber() {
        return citizenshipNumber;
    }

    public void setCitizenshipNumber(String citizenshipNumber) {
        this.citizenshipNumber = citizenshipNumber;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "FutsalDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
