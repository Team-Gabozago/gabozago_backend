package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthLoginResponse {
    private final String message;
    private final String code;

    public AuthLoginResponse(String message) {
        this.message = message;
        this.code = "LOGIN_SUCCESS";
    }

    public static AuthLoginResponse of(String message) {
        return new AuthLoginResponse(message);
    }
}
