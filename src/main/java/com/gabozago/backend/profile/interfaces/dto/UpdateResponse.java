package com.gabozago.backend.profile.interfaces.dto;

import lombok.Getter;

@Getter
public class UpdateResponse {
    private final String message;
    private final String code;

    public UpdateResponse(String message) {
        this.message = message;
        this.code = "PROFILE_UPDATED";
    }

    public static UpdateResponse of(String message) {
        return new UpdateResponse(message);
    }
}
