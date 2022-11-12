package com.gabozago.backend.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements EnumModel {

    // Join
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "DUPLICATED_EMAIL", "이미 존재하는 이메일입니다."),
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "DUPLICATED_NICKNAME", "이미 존재하는 닉네임입니다."),

    // Auth
    UNAUTHENTICATED(HttpStatus.FORBIDDEN, "UNAUTHENTICATED", "인증되지 않은 사용자입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "권한이 없는 사용자입니다."),
    PASSWORD_WRONG(HttpStatus.UNAUTHORIZED, "PASSWORD_WRONG", "비밀번호가 틀렸습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "존재하지 않는 사용자입니다.");


    private HttpStatus status;
    private String code;
    private String message;

    ErrorCode(HttpStatus status, String code, String message) {
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