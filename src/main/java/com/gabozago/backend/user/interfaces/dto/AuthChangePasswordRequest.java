package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AuthChangePasswordRequest {
    public static final String PASSWORD_NOT_NULL = "password는 필수 입력값입니다.";
    public static final String NEW_PASSWORD_NOT_NULL = "newPassword는 필수 입력값입니다.";

    @NotBlank(message = PASSWORD_NOT_NULL)
    private String password;
    @NotBlank(message = NEW_PASSWORD_NOT_NULL)
    private String newPassword;
}
