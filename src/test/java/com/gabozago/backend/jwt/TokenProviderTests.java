package com.gabozago.backend.jwt;

import ch.qos.logback.classic.util.ContextInitializer;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.service.UserService;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("Token Provider Tests")
public class TokenProviderTests {
    TokenProvider tokenProvider;

    @Mock
    UserService userService;
    User user;

    @BeforeEach
    public void init() {
        user = User.builder().id(1L).email("test@example.com").username("user").password("password").build();
        userService = Mockito.mock(UserService.class);
        tokenProvider = new TokenProvider(userService);
    }

    @Test
    void testCreateToken() {
        tokenProvider.createToken(1L, null);
    }

    @Test
    void testCreateTokenWithRoles() {
        tokenProvider.createToken(1L, List.of("ROLE_USER"));
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

    @Test
    void testGetAuthentication() {
        Mockito.when(userService.loadUserByUsername(user.getId().toString())).thenReturn(user);

        String token = tokenProvider.createToken(1L, null);
        Authentication authentication = tokenProvider.getAuthentication(token);
        assertEquals("user", authentication.getName());
    }

    @Test
    void testGetAuthenticationWithInvalidToken() {
        tokenProvider.getAuthentication("invalid token");
    }
}

