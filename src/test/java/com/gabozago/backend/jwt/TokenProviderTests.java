package com.gabozago.backend.jwt;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TokenProviderTests {
    TokenProvider tokenProvider;

    @BeforeEach
    public void init() {
        tokenProvider = new TokenProvider("secret");
    }

    @Test
    void testCreateToken() {
        String token = tokenProvider.createToken(1L, null);
    }

    @Test
    void testCreateTokenWithRoles() {
        String token = tokenProvider.createToken(1L, List.of("ROLE_USER"));
    }

    @Test
    void testGetUserID() {
        String token = tokenProvider.createToken(1L, null);
        Long userID = tokenProvider.getUserID(token);
        assertEquals(1L, userID);
    }

    @Test
    void testValidateToken() {
        String token = tokenProvider.createToken(1L, null);
        Boolean isValid = tokenProvider.validateToken(token);
        assertEquals(true, isValid);
    }

    @Test
    void testValidateTokenWithInvalidToken() {
        Boolean isValid = tokenProvider.validateToken("invalid token");
        assertEquals(false, isValid);
    }
}

