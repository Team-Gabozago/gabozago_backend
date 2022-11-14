package com.gabozago.backend.jwt;


import com.gabozago.backend.entity.RefreshToken;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Token Provider Tests")
public class TokenProviderTests {
    TokenProvider tokenProvider;

    @Mock
    UserService userService;
    User testUser;

    @BeforeEach
    public void init() {
        testUser = User.builder().id(1L).email("test@example.com").username("user").password("password").build();
        userService = Mockito.mock(UserService.class);
        tokenProvider = new TokenProvider(userService);
    }

    @Test
    @DisplayName("Access 토큰을 생성한다.")
    void testCreateAccessToken() {
        String accessToken = tokenProvider.createAccessToken(1L, List.of("ROLE_USER"));

        assertTrue(tokenProvider.validateToken(accessToken));
    }

    @Test
    @DisplayName("Refresh 토큰을 생성한다.")
    void testCreateRefreshToken() {
        String refreshToken = tokenProvider.createRefreshToken(1L);

        assertTrue(tokenProvider.validateToken(refreshToken));
    }

    @Test
    @DisplayName("Refresh 토큰을 생성하고, RefreshToken 엔티티를 생성한다.")
    void testGenerateNewRefreshToken() {
        RefreshToken refreshToken = tokenProvider.createRefreshTokenEntity(testUser);
        assertEquals(refreshToken.getUser(), testUser);

        assertTrue(tokenProvider.validateToken(refreshToken.getToken()));
    }

    @Test
    @DisplayName("토큰에서 만료 시간을 가져온다.")
    void testGetExpirationDateFromToken() {
        String token = tokenProvider.createAccessToken(testUser.getId(), testUser.getRoles());
        Timestamp timestamp = tokenProvider.getExpirationDateFromToken(token);

        assertTrue(timestamp.after(new Timestamp(System.currentTimeMillis())));
    }

    @Test
    @DisplayName("토큰이 유효한지 검사한다 - 성공")
    void testValidateToken() {
        String token = tokenProvider.createAccessToken(1L, null);
        Boolean isValid = tokenProvider.validateToken(token);
        assertEquals(true, isValid);
    }

    @Test
    @DisplayName("토큰이 유효한지 검사한다 - 실패")
    void testValidateTokenWithInvalidToken() {
        Boolean isValid = tokenProvider.validateToken("invalid token");
        assertEquals(false, isValid);
    }

    @Test
    @DisplayName("토큰에서 인증 정보를 가져온다.")
    void testGetAuthentication() {
        Mockito.when(userService.loadUserByUsername(testUser.getId().toString())).thenReturn(testUser);

        String token = tokenProvider.createAccessToken(1L, null);
        Authentication authentication = tokenProvider.getAuthentication(token);
        assertEquals("user", authentication.getName());
    }

    @Test
    @DisplayName("토큰에서 인증 정보를 가져온다. - 실패")
    void testGetAuthenticationWithInvalidToken() {
        tokenProvider.getAuthentication("invalid token");
    }

    @Test
    @DisplayName("토큰 갱신")
    void testRefreshAccessToken() {
        RefreshToken refreshToken = tokenProvider.createRefreshTokenEntity(testUser);
        String refreshedToken = tokenProvider.refreshAccessToken(refreshToken);

        assertTrue(tokenProvider.validateToken(refreshedToken));
    }
}

