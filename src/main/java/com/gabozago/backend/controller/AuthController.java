package com.gabozago.backend.controller;

import com.gabozago.backend.entity.User;
import com.gabozago.backend.error.ErrorCode;
import com.gabozago.backend.error.ErrorResponse;
import com.gabozago.backend.jwt.TokenProvider;
import com.gabozago.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private final TokenProvider tokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, TokenProvider tokenProvider, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("this is auth controller");
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody Map<String, String> user) {
        if (userService.checkExistsByEmail(user.get("email"))) {
            return ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL).entity();
        }

        if (userService.checkExistsByNickname(user.get("nickname"))) {
            return ErrorResponse.of(ErrorCode.DUPLICATED_NICKNAME).entity();
        }

        userService.save(User.builder()
                .email(user.get("email"))
                .password(passwordEncoder.encode(user.get("password")))
                .roles(Collections.singletonList("ROLE_USER"))
                .build());

        return ResponseEntity.ok("join success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        User user;

        try {
            user = userService.findByEmail(email);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

        if (!passwordEncoder.matches(request.get("password"), user.getPassword())) {
            return new ResponseEntity<>("password is not correct", null, 401);
        }

        String jwtToken = tokenProvider.createToken(user.getId(), user.getRoles());

        return ResponseEntity.ok(jwtToken);
    }

    @GetMapping("/needAuth")
    public ResponseEntity<String> needAuth() {
        return ResponseEntity.ok("auth success");
    }
}
