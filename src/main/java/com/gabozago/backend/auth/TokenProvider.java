package com.gabozago.backend.auth;

import com.gabozago.backend.user.domain.RefreshToken;
import com.gabozago.backend.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {
    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final UserDetailsService userDetailsService;
    private String secretKey = "secret";

    @PostConstruct
    protected void init() {
        logger.info("Secret key: " + secretKey);
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(Long userID, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userID.toString());
        claims.put("roles", roles);

        long expiration = 1000 * 60 * 60 * 2; // 2 hours
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken(Long userID) {
        Claims claims = Jwts.claims().setSubject(userID.toString());

        long expiration = 1000L * 60 * 60 * 24 * 30; // 30 days
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public RefreshToken createRefreshTokenEntity(User user) {
        String token = createRefreshToken(user.getId());

        return RefreshToken
                .builder()
                .token(token)
                .user(user)
                .expiredAt(getExpirationDateFromToken(token))
                .isValid(true)
                .build();
    }

    public Timestamp getExpirationDateFromToken(String token) {
        return new Timestamp(Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime()
        );
    }

    public Boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        }
        return false;
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        if (!validateToken(token)) {
            return null;
        }

        UserDetails userDetails;

        try {
            userDetails = userDetailsService.loadUserByUsername(this.getUserIdFromToken(token));
        } catch (Exception e) {
            logger.error("Get User Failed: {}", e.getMessage());

            return null;
        }

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserIdFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public String refreshAccessToken(RefreshToken refreshToken) {
        String accessToken = null;

        if (refreshToken.getExpiredAt().after(new Timestamp(System.currentTimeMillis()))) {
            accessToken = createAccessToken(refreshToken.getUser().getId(), refreshToken.getUser().getRoles());
        }

        return accessToken;
    }
}
