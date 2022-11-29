package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthJoinResponse {
    private final String message;
    private final String code;

    public AuthJoinResponse(String message) {
        this.message = message;
        this.code = "USER_CREATED";
    }

    public static AuthJoinResponse of(String message) {
        return new AuthJoinResponse(message);
    }
}
