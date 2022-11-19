package com.gabozago.backend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements EnumModel {

    // Join
    DUPLICATED_EMAIL("DUPLICATED_EMAIL", "It's a email that already exists."),
    DUPLICATED_NICKNAME("DUPLICATED_NICKNAME", "It's a nickname that already exists."),

    // Auth
    UNAUTHENTICATED("UNAUTHENTICATED", "This user is not authenticated."),
    UNAUTHORIZED("UNAUTHORIZED", "You are an unauthorized user."),
    PASSWORD_WRONG("PASSWORD_WRONG", "The password is wrong."),
    USER_NOT_FOUND("USER_NOT_FOUND", "User does not exist."),
    INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", "Invalid refresh token.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getKey() {
        return this.code;
    }

    @Override
    public String getValue() {
        return this.message;
    }
}