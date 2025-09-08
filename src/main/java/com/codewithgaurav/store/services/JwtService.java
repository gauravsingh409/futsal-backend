package com.codewithgaurav.store.services;

import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.codewithgaurav.store.entity.UserEntity;
import com.codewithgaurav.store.exception.UnauthorizedException;
import com.codewithgaurav.store.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private Key getKey() {
        // keyshmacShaKeyFor -> build a cryptographic key signing the toke
        // SECRET.getBytes() -> turns a string into byte array
        String SECRET = "my-secret-key-my-secret-key-my-secret-key";
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String extractUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long extractIdFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("id",
                Long.class);
    }

    // Extract the id from httpServeletRequest
    public Long extractId(HttpServletRequest httpServletRequest) {
        String token = this.extractToken(httpServletRequest);
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("id", Long.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired jwt token");
        }
    }

    // is valid user
    public boolean isValidUser(HttpServletRequest httpServletRequest) {
        String token = this.extractToken(httpServletRequest);
        return StringUtils.hasText(token) ? true : false;
    }

    // is logged user is admin
    public boolean isAdmin(Long userId) {
        UserEntity user = userRepository.findById(userId).get();
        return user.isAdmin();
    }

    // is logged user is owner
    public boolean isOwner(Long userId) {
        UserEntity user = userRepository.findById(userId).get();
        return user.isOwner();
    }

    // extract token
    public String extractToken(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Token not provided.");
        String token = authHeader.substring(7);
        return token;
    }

    // Generate Access Token: valid for 5 minutes
    public String generateAccessToken(String username, Long id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 1000)) // 5
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate Refresh Token: valid for 7 days
    public String generateRefreshToken(String username, Long id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // is valid user
    public Long extractValidUserId(HttpServletRequest httpServletRequest) {
        Long id = this.extractId(httpServletRequest);
        return id > 0 ? id : null;
    }

    // Extract owner id
    public Long extractValidOwnerId(HttpServletRequest httpServletRequest) {
        Long id = this.extractId(httpServletRequest);
        if (this.isOwner(id))
            return id;
        else
            throw new UnauthorizedException("Permission not allowed");
    }

    public Long extractValidAdminId(HttpServletRequest httpServletRequest) {
        Long id = this.extractId(httpServletRequest);
        if (this.isAdmin(id))
            return id;
        else
            throw new UnauthorizedException("Permission not allowed");
    }

    // is valid owner or admin
    public boolean isValidAdminOrOwner(HttpServletRequest httpServletRequest) {
        Long id = this.extractId(httpServletRequest);
        if (this.isAdmin(id) || this.isOwner(id))
            return true;
        else
            return false;
    }

}
