package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthChangePasswordResponse {
    private final String message;
    private final String code;

    public AuthChangePasswordResponse(String message) {
        this.message = message;
        this.code = "PASSWORD_CHANGED";
    }

    public static AuthChangePasswordResponse of(String message) {
        return new AuthChangePasswordResponse(message);
    }
}
