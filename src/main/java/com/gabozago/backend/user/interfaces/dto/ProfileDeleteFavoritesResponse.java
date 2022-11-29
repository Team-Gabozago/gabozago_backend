package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

@Getter
public class ProfileDeleteFavoritesResponse {
    private final String message;
    private final String code;

    public ProfileDeleteFavoritesResponse(String message) {
        this.message = message;
        this.code = "FAVORITE_DELETED";
    }

    public static ProfileDeleteFavoritesResponse of(String message) {
        return new ProfileDeleteFavoritesResponse(message);
    }
}
