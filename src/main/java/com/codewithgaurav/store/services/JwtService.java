package com.codewithgaurav.store.services;

import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
   private final String SECRET = "my-secret-key-my-secret-key-my-secret-key";

   private Key getKey() {
      // keyshmacShaKeyFor -> build a cryptographic key signing the toke
      // SECRET.getBytes() -> turns a string into byte array
      return Keys.hmacShaKeyFor(SECRET.getBytes());
   }

   public String generateToken(String username) {
      return Jwts.builder() // Start building jwt using jwt.builder()
            .setSubject(username) // sets the subject or data of the token - we store the username here
            .setIssuedAt(new Date()) // currently issue time
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour after the token creaed
            .signWith(getKey(), SignatureAlgorithm.HS256) // algorithm with secreatkey
            .compact(); // convert the token to compact, URL safe string - ready to send to frontend
   }

   public String extractUsername(String token) {
      return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
   }
}
