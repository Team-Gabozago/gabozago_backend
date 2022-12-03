package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserAreasResponse {
    private final String message;
    private final String code;
    private final List<UserAreaResponse> areas;

    public UserAreasResponse(String message, List<UserAreaResponse> areas) {
        this.message = message;
        this.code = "USER_AREAS";
        this.areas = areas;
    }

    public static UserAreasResponse of(String message, List<UserAreaResponse> areas) {
        return new UserAreasResponse(message, areas);
    }
}
