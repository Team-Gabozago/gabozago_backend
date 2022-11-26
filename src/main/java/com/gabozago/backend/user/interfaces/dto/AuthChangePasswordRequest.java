package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class AuthChangePasswordRequest {
    public static final String PASSWORD_NOT_NULL = "password는 필수 입력값입니다.";
    public static final String NEW_PASSWORD_NOT_NULL = "newPassword는 필수 입력값입니다.";

    private String password;
    private String newPassword;
}
