package com.gabozago.backend.profile.interfaces.dto;

import lombok.Getter;

@Getter
public class DeleteFavoritesResponse {
    private final String message;
    private final String code;

    public DeleteFavoritesResponse(String message) {
        this.message = message;
        this.code = "FAVORITE_DELETED";
    }

    public static DeleteFavoritesResponse of(String message) {
        return new DeleteFavoritesResponse(message);
    }
}
