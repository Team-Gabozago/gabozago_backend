package com.gabozago.backend.dto.auth;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class JoinRequestDto {
    public static final String EMAIL_NOT_NULL = "email은 필수 입력값입니다.";
    public static final String PASSWORD_NOT_NULL = "password는 필수 입력값입니다.";
    public static final String NICKNAME_NOT_NULL = "nickname은 필수 입력값입니다.";

    @NotBlank(message = EMAIL_NOT_NULL)
    private String email;

    @NotBlank(message = PASSWORD_NOT_NULL)
    private String password;

    @NotBlank(message = NICKNAME_NOT_NULL)
    private String nickname;
}
