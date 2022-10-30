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
        String token = tokenProvider.createToken("test", null);
    }

    @Test
    void testCreateTokenWithRoles() {
        String token = tokenProvider.createToken("test", List.of("ROLE_USER"));
    }
}

