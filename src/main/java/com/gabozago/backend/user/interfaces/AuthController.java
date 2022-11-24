package com.gabozago.backend.user.interfaces;

import com.gabozago.backend.user.interfaces.dto.auth.JoinRequestDto;
import com.gabozago.backend.user.interfaces.dto.auth.LoginRequestDto;
import com.gabozago.backend.user.domain.RefreshToken;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.error.ErrorResponse;
import com.gabozago.backend.auth.header.AuthHttpHeaders;
import com.gabozago.backend.auth.TokenProvider;
import com.gabozago.backend.user.service.RefreshTokenService;
import com.gabozago.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @PostMapping(path = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> join(final @Valid @RequestBody JoinRequestDto user) {
        if (userService.checkExistsByEmail(user.getEmail())) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATED_EMAIL).parseJson(), HttpStatus.CONFLICT);
        }

        if (userService.checkExistsByNickname(user.getNickname())) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATED_NICKNAME).parseJson(), HttpStatus.CONFLICT);
        }

        userService.save(User.builder()
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .nickname(user.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return new ResponseEntity<>("{\"message\": \"login success\"}", HttpStatus.OK);
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(final @Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
        String email = loginRequestDto.getEmail();
        User user;

        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.USER_NOT_FOUND).parseJson(), HttpStatus.NOT_FOUND);
        }

        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.PASSWORD_WRONG).parseJson(), HttpStatus.UNAUTHORIZED);
        }

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRoles());
        RefreshToken refreshToken = tokenProvider.createRefreshTokenEntity(user);

        refreshTokenService.save(refreshToken);

        AuthHttpHeaders headers = new AuthHttpHeaders();

        headers.setAccessToken(accessToken, request);
        headers.setRefreshToken(refreshToken.getToken(), request);

        return new ResponseEntity<>("{\"message\": \"login success\"}", headers, HttpStatus.OK);
    }

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request) {
        RefreshToken token;

        try {
            token = refreshTokenService.findByToken(refreshToken);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_REFRESH_TOKEN).parseJson(), HttpStatus.UNAUTHORIZED);
        }

        String accessToken = tokenProvider.refreshAccessToken(token);

        AuthHttpHeaders headers = new AuthHttpHeaders();

        headers.setAccessToken(accessToken, request);

        return new ResponseEntity<>("{\"message\": \"refresh success\"}", headers, HttpStatus.OK);
    }

    @GetMapping(value = "/needAuth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> needAuth() {
        return ResponseEntity.ok("{\"message\": \"auth success\"}");
    }
}
