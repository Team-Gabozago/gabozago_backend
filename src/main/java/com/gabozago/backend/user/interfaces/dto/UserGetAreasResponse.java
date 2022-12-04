package com.gabozago.backend.user.interfaces.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class UserGetAreasResponse {
    private final String message;
    private final String code;
    private final List<UserAreaResponse> areas;

    public UserGetAreasResponse(String message, List<UserAreaResponse> areas) {
        this.message = message;
        this.code = "USER_AREAS";
        this.areas = areas;
    }

    public static UserGetAreasResponse of(String message, List<UserAreaResponse> areas) {
        return new UserGetAreasResponse(message, areas);
    }
}
