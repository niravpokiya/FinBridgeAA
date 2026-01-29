package com.FinBridgeAA.Auth_service.Services;

import com.FinBridgeAA.Auth_service.Entity.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateJWTToken(AuthUser user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole());
        claims.put("phone", user.getPhoneNumber());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getAuthUserId().toString()) // PRIMARY IDENTITY
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }


    public String extractUserId(String token) {
        return extractAllClaims(token).getSubject();
    }
//
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

//    public boolean validateToken(String token) {
//        try {
//            extractAllClaims(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    private boolean isTokenExpired(String token) {
        return extractAllClaims(token)
                .getExpiration()
                .before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
