package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class ProfileDeleteResponse {
    private final String message;
    private final String code;

    public ProfileDeleteResponse(String message) {
        this.message = message;
        this.code = "PROFILE_DELETED";
    }

    public static ProfileDeleteResponse of(String message) {
        return new ProfileDeleteResponse(message);
    }
}
