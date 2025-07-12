package com.codewithgaurav.store.services;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import com.codewithgaurav.store.exception.UnauthorizedException;
import com.codewithgaurav.store.model.UserModel;
import com.codewithgaurav.store.repository.UserRepository;
import com.codewithgaurav.store.security.AuthResult;
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

    // public String generateToken(String username, String id) {
    // return Jwts.builder() // Start building jwt using jwt.builder()
    // .setSubject(username) // sets the subject or data of the token - It is
    // default claim(data) that it
    // // accept
    // .claim("id", id) // if we need to store other field too then we can manually
    // add our claim (data)
    // .setIssuedAt(new Date()) // currently issue time
    // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 *
    // 7)) // 1 hour after the token
    // // creaed
    // .signWith(getKey(), SignatureAlgorithm.HS256) // algorithm with secreatkey
    // .compact(); // convert the token to compact, URL safe string - ready to send
    // to frontend
    // }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Extract the id from token
    public String extractId(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("id", String.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid or expired jwt token");
        }
    }

    // accept the token and extract the id from token
    public AuthResult extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            return new AuthResult(false, null, "Authorization not provided");
        }

        String token = authHeader.substring(7);
        try {
            String userId = this.extractId(token);
            return new AuthResult(true, userId, null);
        } catch (Exception e) {
            return new AuthResult(false, null, token + " is not a valid authorization token");
        }
    }

    // is logged user is admin
    public boolean isAdmin(String userId) {
        UserModel user = userRepository.findById(userId).get();
        return user.isIs_admin();
    }

    // extract token
    public String extractToken(HttpServletRequest httpServletRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer"))
            return "";
        String token = authHeader.substring(7);
        return token;
    }

    // Generate Access Token: valid for 5 minutes
    public String generateAccessToken(String username, String id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 100)) // 5
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Generate Refresh Token: valid for 7 days
    public String generateRefreshToken(String username, String id) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // 7 days
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

}
