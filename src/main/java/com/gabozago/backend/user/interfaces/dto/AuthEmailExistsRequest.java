package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class AuthEmailExistsRequest {
    private static final String EMAIL_NOT_NULL = "email은 필수 입력값입니다.";

    @NotBlank(message = EMAIL_NOT_NULL)
    private String email;
}
