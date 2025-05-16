package com.codewithgaurav.store.dto;

import com.codewithgaurav.store.validation.FutsalLoginGroup;
import jakarta.validation.constraints.*;

public class FutsalDto {

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

    // Optional: toString() for debugging
    @Override
    public String toString() {
        return "FutsalDto{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
