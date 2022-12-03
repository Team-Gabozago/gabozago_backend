package com.gabozago.backend.profile.interfaces.dto;

import lombok.Getter;

@Getter
public class DeleteResponse {
    private final String message;
    private final String code;

    public DeleteResponse(String message) {
        this.message = message;
        this.code = "PROFILE_DELETED";
    }

    public static DeleteResponse of(String message) {
        return new DeleteResponse(message);
    }
}
