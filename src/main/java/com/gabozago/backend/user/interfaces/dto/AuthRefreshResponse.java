package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthRefreshResponse {
    private final String message;
    private final String code;

    public AuthRefreshResponse(String message) {
        this.message = message;
        this.code = "REFRESH_SUCCESS";
    }

    public static AuthRefreshResponse of(String message) {
        return new AuthRefreshResponse(message);
    }
}
