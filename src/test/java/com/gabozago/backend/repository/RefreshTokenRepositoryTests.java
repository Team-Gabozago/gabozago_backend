package com.gabozago.backend.repository;

import com.gabozago.backend.entity.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class RefreshTokenRepositoryTests {
    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Test
    @DisplayName("토큰 만료시간 체크")
    void testFindByTokenAndNotExpiredAndIsValid() {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token("test")
                .expiredAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .build();

        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> result = refreshTokenRepository.findByTokenAndExpiredAtBefore("test", Timestamp.valueOf("2023-01-01 00:00:00"));
        assert result.isPresent();
    }

    @Test
    @DisplayName("토큰 만료시간 체크 - 지남")
    void testFindByTokenAndExpiredAndIsValid() {
        RefreshToken refreshToken = RefreshToken
                .builder()
                .token("test")
                .expiredAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .build();

        refreshTokenRepository.save(refreshToken);

        Optional<RefreshToken> result = refreshTokenRepository.findByTokenAndExpiredAtBefore("test", Timestamp.valueOf("2021-01-01 00:00:00"));
        assert result.isEmpty();
    }
}
