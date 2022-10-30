package com.gabozago.backend.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final String secretKey;

    public TokenProvider(@Value("${jwt.secret}") String secret) {
        this.secretKey = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String createToken(String userID, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userID);
        claims.put("roles", roles);

        // 7 days
        long tokenLifeTime = 1000 * 60 * 60 * 24 * 7;

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenLifeTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
