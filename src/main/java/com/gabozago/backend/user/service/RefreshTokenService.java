package com.gabozago.backend.user.service;

import com.gabozago.backend.user.domain.RefreshToken;
import com.gabozago.backend.user.infrastructure.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    public void save(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepository
                .findByTokenAndExpiredAtBefore(refreshToken, new Timestamp(System.currentTimeMillis()))
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));
    }
}
