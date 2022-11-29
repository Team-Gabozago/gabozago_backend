package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthEmailExistsResponse {
    private final boolean exists;
    private final String message;

    public AuthEmailExistsResponse(boolean exists, String message) {
        this.exists = exists;
        this.message = message;
    }

    public static AuthEmailExistsResponse of(boolean exists) {
        if (exists) {
            return new AuthEmailExistsResponse(true, "이미 존재하는 이메일입니다");
        } else {
            return new AuthEmailExistsResponse(false, "사용 가능한 이메일입니다");
        }
    }
}
