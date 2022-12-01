package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class ProfileUpdateFavoritesResponse {
    private final String message;
    private final String code;

    public ProfileUpdateFavoritesResponse(String message) {
        this.message = message;
        this.code = "FAVORITE_UPDATED";
    }

    public static ProfileUpdateFavoritesResponse of(String message) {
        return new ProfileUpdateFavoritesResponse(message);
    }
}
