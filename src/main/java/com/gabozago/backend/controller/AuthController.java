package com.gabozago.backend.controller;

import com.gabozago.backend.dto.auth.JoinRequestDto;
import com.gabozago.backend.dto.auth.LoginRequestDto;
import com.gabozago.backend.entity.RefreshToken;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.error.ErrorResponse;
import com.gabozago.backend.http.header.AuthHttpHeaders;
import com.gabozago.backend.jwt.TokenProvider;
import com.gabozago.backend.service.RefreshTokenService;
import com.gabozago.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("this is auth controller");
    }

    @PostMapping("/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> join(final @Valid @RequestBody JoinRequestDto user) {
        if (userService.checkExistsByEmail(user.getEmail())) {
            return ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL).entity();
        }

        if (userService.checkExistsByNickname(user.getNickname())) {
            return ErrorResponse.of(ErrorCode.DUPLICATED_NICKNAME).entity();
        }

        userService.save(User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .nickname(user.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return ResponseEntity.ok("join success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(final @Valid @RequestBody LoginRequestDto request) {
        String email = request.getEmail();
        User user;

        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            return ErrorResponse.of(ErrorCode.USER_NOT_FOUND).entity();
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ErrorResponse.of(ErrorCode.PASSWORD_WRONG).entity();
        }

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoles());
        RefreshToken refreshToken = tokenProvider.createRefreshTokenEntity(user);

        refreshTokenService.save(refreshToken);

        AuthHttpHeaders headers = new AuthHttpHeaders();

        headers.setAccessToken(accessToken);
        headers.setRefreshToken(refreshToken.getToken());

        return new ResponseEntity<>("login success", headers, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@CookieValue("refreshToken") String refreshToken) {
        RefreshToken token;

        try {
            token = refreshTokenService.findByToken(refreshToken);
        } catch (Exception e) {
            return ErrorResponse.of(ErrorCode.INVALID_REFRESH_TOKEN).entity();
        }

        String accessToken = tokenProvider.refreshAccessToken(token);

        AuthHttpHeaders headers = new AuthHttpHeaders();

        headers.setAccessToken(accessToken);

        return new ResponseEntity<>("refresh success", headers, HttpStatus.OK);
    }

    @GetMapping("/needAuth")
    public ResponseEntity<String> needAuth() {
        return ResponseEntity.ok("auth success");
    }
}
