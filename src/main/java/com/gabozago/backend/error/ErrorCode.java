package com.gabozago.backend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements EnumModel {

    // Join
    DUPLICATED_EMAIL(HttpServletResponse.SC_CONFLICT, "DUPLICATED_EMAIL", "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(HttpServletResponse.SC_CONFLICT, "DUPLICATED_NICKNAME", "이미 존재하는 닉네임입니다."),

    // Auth
    UNAUTHENTICATED(HttpServletResponse.SC_FORBIDDEN, "UNAUTHENTICATED", "인증되지 않은 사용자입니다."),
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "권한이 없는 사용자입니다.");


    private int status;
    private String code;
    private String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
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