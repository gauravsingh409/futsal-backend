package com.gaurav.futsal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String first_name;

    private String last_name;

    private String profile;


    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

    // Enums defined in the same file
    public enum AuthProvider {
        GOOGLE, FACEBOOK, GITHUB, LOCAL
    }

    public enum UserRole {
        USER, OWNER, ADMIN
    }
}

