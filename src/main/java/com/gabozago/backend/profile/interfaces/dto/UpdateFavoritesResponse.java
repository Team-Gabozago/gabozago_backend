package com.gabozago.backend.profile.interfaces.dto;

import lombok.Getter;

@Getter
public class UpdateFavoritesResponse {
    private final String message;
    private final String code;

    public UpdateFavoritesResponse(String message) {
        this.message = message;
        this.code = "FAVORITE_UPDATED";
    }

    public static UpdateFavoritesResponse of(String message) {
        return new UpdateFavoritesResponse(message);
    }
}
