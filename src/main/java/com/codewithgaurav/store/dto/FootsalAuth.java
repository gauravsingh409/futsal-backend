package com.codewithgaurav.store.dto;

import com.codewithgaurav.store.validation.FutsalLoginGroup;
import jakarta.validation.constraints.*;

public class FootsalAuth {

    @NotBlank(message = "Username field is missing", groups = { FutsalLoginGroup.class })
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters", groups = {
            FutsalLoginGroup.class })
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores", groups = {
            FutsalLoginGroup.class })
    private String username;

    @NotBlank(message = "Password field is missing", groups = {
            FutsalLoginGroup.class })
    @Size(min = 6, message = "Password must be at least 6 characters long", groups = {
            FutsalLoginGroup.class })
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "Password must contain uppercase, lowercase, number, and special character", groups = {
            FutsalLoginGroup.class })
    private String password;

    @NotBlank(message = "Futsal name is missing")
    @Size(max = 50, message = "Futsal name must be less than 50 characters")
    private String footsal_name;

    @NotBlank(message = "Registration number is missing")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Registration number must be alphanumeric and can include hyphens")
    private String registration_number;

    @NotBlank(message = "Location is missing")
    @Size(min = 3, max = 100, message = "Location must be between 3 and 100 characters")
    private String location;

    @NotBlank(message = "Price per hour is missing")
    @Pattern(regexp = "^[1-9][0-9]{1,4}$", message = "Price must be a number between 10 and 99999")
    private String price_per_hour;

    @NotBlank(message = "Contact info is missing")
    @Pattern(regexp = "^[0-9]{10}$", message = "Contact number must be exactly 10 digits")
    private String contact_info;

    @NotBlank(message = "Image URL is missing")
    @Pattern(regexp = ".*\\.(jpg|jpeg|png)$", message = "Only JPG, JPEG, or PNG image formats are allowed")
    private String image;

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

    public String getFootsal_name() {
        return footsal_name;
    }

    public void setFootsal_name(String footsal_name) {
        this.footsal_name = footsal_name;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice_per_hour() {
        return price_per_hour;
    }

    public void setPrice_per_hour(String price_per_hour) {
        this.price_per_hour = price_per_hour;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "FootsalAuth{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", footsal_name='" + footsal_name + '\'' +
                ", registration_number='" + registration_number + '\'' +
                ", location='" + location + '\'' +
                ", price_per_hour='" + price_per_hour + '\'' +
                ", contact_info='" + contact_info + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}