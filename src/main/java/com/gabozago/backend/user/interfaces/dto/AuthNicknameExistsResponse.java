package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthNicknameExistsResponse {
    private final boolean exists;
    private final String code;
    private final String message;

    public AuthNicknameExistsResponse(boolean exists, String message) {
        this.exists = exists;
        if (exists) {
            this.code = "NICKNAME_EXISTS";
        } else {
            this.code = "AVAILABLE_NICKNAME";
        }
        this.message = message;
    }

    public static AuthNicknameExistsResponse of(boolean exists) {
        if (exists) {
            return new AuthNicknameExistsResponse(true, "이미 존재하는 닉네임입니다");
        } else {
            return new AuthNicknameExistsResponse(false, "사용 가능한 닉네임입니다");
        }
    }
}
