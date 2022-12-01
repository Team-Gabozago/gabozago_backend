package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class ProfileUpdateResponse {
    private final String message;
    private final String code;

    public ProfileUpdateResponse(String message) {
        this.message = message;
        this.code = "PROFILE_UPDATED";
    }

    public static ProfileUpdateResponse of(String message) {
        return new ProfileUpdateResponse(message);
    }
}
