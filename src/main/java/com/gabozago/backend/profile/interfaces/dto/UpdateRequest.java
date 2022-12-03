package com.gabozago.backend.profile.interfaces.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UpdateRequest {
    public static final String NICKNAME_NOT_NULL = "nickname은 필수 입력값입니다.";

    @NotBlank(message = NICKNAME_NOT_NULL)
    private String nickname;
}
