package com.gabozago.backend.jwt;

import ch.qos.logback.classic.util.ContextInitializer;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.service.UserService;
import org.apache.tomcat.util.file.ConfigFileLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DisplayName("Token Provider Tests")
public class TokenProviderTests {
    TokenProvider tokenProvider;
    User user;

    @BeforeEach
    public void init() {
        user = User.builder().id(1L).email("test@example.com").username("user").password("password").build();
        tokenProvider = new TokenProvider(new UserService(null) {
            @Override
            public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                return user;
            }

            @Override
            public void save(User user) {
                // do nothing
            }

            @Override
            public User findByEmail(String email) {
                return user;
            }
        });


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
        System.out.println(user.getId());
        String token = tokenProvider.createToken(user.getId(), null);
        tokenProvider.getAuthentication(token);
    }

    @Test
    void testGetAuthenticationWithInvalidToken() {
        tokenProvider.getAuthentication("invalid token");
    }
}

