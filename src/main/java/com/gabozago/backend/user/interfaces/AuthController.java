package com.gabozago.backend.user.interfaces;

import com.gabozago.backend.profile.domain.ProfileImage;
import com.gabozago.backend.profile.service.ProfileService;
import com.gabozago.backend.user.interfaces.dto.*;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    private final ProfileService profileService;

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("this is auth controller");
    }

    @PostMapping("/email-exists")
    public ResponseEntity<AuthEmailExistsResponse> emailExists(@Valid @RequestBody AuthEmailExistsRequest user) {
        if (userService.checkExistsByEmail(user.getEmail())) {
            return new ResponseEntity<>(AuthEmailExistsResponse.of(true), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(AuthEmailExistsResponse.of(false), HttpStatus.OK);
    }

    @PostMapping("/nickname-exists")
    public ResponseEntity<AuthNicknameExistsResponse> nicknameExists(@Valid @RequestBody AuthNicknameExistsRequest user) {
        if (userService.checkExistsByNickname(user.getNickname())) {
            return new ResponseEntity<>(AuthNicknameExistsResponse.of(true), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(AuthNicknameExistsResponse.of(false), HttpStatus.OK);
    }

    @PostMapping(path = "/join", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> join(final @Valid @RequestBody JoinRequestDto request) {
        if (userService.checkExistsByEmail(request.getEmail())) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATED_EMAIL).parseJson(), HttpStatus.CONFLICT);
        }

        if (userService.checkExistsByNickname(request.getNickname())) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.DUPLICATED_NICKNAME).parseJson(), HttpStatus.CONFLICT);
        }

        ProfileImage profileImage = ProfileImage
                .builder()
                .fileName("gopher.png")
                .size(40627L)
                .path("/uploads/user/profiles/gopher.png")
                .contentType("image/png")
                .build();

        profileService.saveProfileImage(profileImage);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .roles(Collections.singletonList("ROLE_USER"))
                .profileImage(profileImage)
                .build();

        userService.save(user);

        return ResponseEntity.ok(AuthJoinResponse.of("회원가입이 완료되었습니다."));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> login(final @Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request) {
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

        return new ResponseEntity<>(AuthLoginResponse.of("로그인에 성공하였습니다."), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> refresh(@CookieValue("refreshToken") String refreshToken, HttpServletRequest request) {
        RefreshToken token;

        try {
            token = refreshTokenService.findByToken(refreshToken);
        } catch (Exception e) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.INVALID_REFRESH_TOKEN).parseJson(), HttpStatus.UNAUTHORIZED);
        }

        String accessToken = tokenProvider.refreshAccessToken(token);

        AuthHttpHeaders headers = new AuthHttpHeaders();

        headers.setAccessToken(accessToken, request);

        return new ResponseEntity<>(AuthRefreshResponse.of("토큰이 갱신되었습니다."), headers, HttpStatus.OK);
    }

    @GetMapping(value = "/needAuth", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> needAuth() {
        return ResponseEntity.ok("{\"message\": \"auth success\"}");
    }

    @PostMapping(value = "/check-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthCheckPasswordResponse> checkPassword(@AuthenticationPrincipal User user, final @Valid @RequestBody AuthCheckPasswordRequest request) {
        boolean isOk = passwordEncoder.matches(request.getPassword(), user.getPassword());

        return ResponseEntity.ok(
                AuthCheckPasswordResponse.of(
                        isOk ? "password correct." : "password incorrect.",
                        isOk
                )
        );
    }

    @PatchMapping(value = "/password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> changePassword(@AuthenticationPrincipal User user, final @Valid @RequestBody AuthChangePasswordRequest request) {
        boolean isOk = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isOk) {
            return new ResponseEntity<>(new ErrorResponse(ErrorCode.PASSWORD_WRONG).parseJson(), HttpStatus.UNAUTHORIZED);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userService.save(user);

        return ResponseEntity.ok(AuthChangePasswordResponse.of("password changed."));
    }
}
