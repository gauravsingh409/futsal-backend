package com.codewithgaurav.store.services;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private Key getKey() {
        // keyshmacShaKeyFor -> build a cryptographic key signing the toke
        // SECRET.getBytes() -> turns a string into byte array
        String SECRET = "my-secret-key-my-secret-key-my-secret-key";
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username, String id) {
        return Jwts.builder() // Start building jwt using jwt.builder()
                .setSubject(username) // sets the subject or data of the token - It is default claim(data) that it
                // accept
                .claim("id", id) // if we need to store other field too then we can manually add our claim (data)
                .setIssuedAt(new Date()) // currently issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 60)) // 1 hour after the token creaed
                .signWith(getKey(), SignatureAlgorithm.HS256) // algorithm with secreatkey
                .compact(); // convert the token to compact, URL safe string - ready to send to frontend
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String extractId(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody().get("id", String.class);
        } catch (JwtException | IllegalArgumentException  e) {
            throw new RuntimeException("Invalid or expired jwt token");
        }

    }
}
