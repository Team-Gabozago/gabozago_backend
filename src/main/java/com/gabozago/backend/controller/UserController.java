package com.gabozago.backend.controller;


import com.gabozago.backend.dto.user.GetMeResponseDto;
import com.gabozago.backend.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> getMe(@AuthenticationPrincipal User user) {

        GetMeResponseDto response = GetMeResponseDto.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .phoneNumber(user.getPhoneNumber())
                .build();

        return new ResponseEntity<>(response.parseJson(), HttpStatus.OK);
    }
}
