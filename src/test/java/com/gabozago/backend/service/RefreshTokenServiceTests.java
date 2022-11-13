package com.gabozago.backend.service;

import com.gabozago.backend.entity.RefreshToken;
import com.gabozago.backend.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

public class RefreshTokenServiceTests {
    RefreshTokenRepository refreshTokenRepository;
    RefreshTokenService refreshTokenService;

    @BeforeEach
    public void setUp() {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        refreshTokenService = new RefreshTokenService(refreshTokenRepository);
    }

    @Test
    @DisplayName("RefreshToken을 저장한다")
    void testSave() {
        RefreshToken refreshToken = new RefreshToken();
        refreshTokenService.save(refreshToken);
        Mockito.verify(refreshTokenRepository).save(refreshToken);
    }

    @Test
    @DisplayName("토큰 가져오기")
    void testFindByToken() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token("test")
                .expiredAt(Timestamp.valueOf("2023-01-01 00:00:00"))
                .isValid(true)
                .user(Mockito.mock(com.gabozago.backend.entity.User.class))
                .build();

        Mockito.when(refreshTokenRepository.findByTokenAndExpiredAtBefore(eq(refreshToken.getToken()), any()))
                .thenReturn(Optional.of(refreshToken));

        refreshTokenService.findByToken(refreshToken.getToken());
    }

    @Test
    @DisplayName("만료된 토큰 가져오기")
    void testFindByToken_만료됨() {
        RefreshToken refreshToken = RefreshToken.builder()
                .token("test")
                .expiredAt(Timestamp.valueOf("2023-01-01 00:00:00"))
                .isValid(true)
                .user(Mockito.mock(com.gabozago.backend.entity.User.class))
                .build();

        Mockito.when(refreshTokenRepository.findByTokenAndExpiredAtBefore(eq(refreshToken.getToken()), any()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> refreshTokenService.findByToken(refreshToken.getToken()));
    }
}
