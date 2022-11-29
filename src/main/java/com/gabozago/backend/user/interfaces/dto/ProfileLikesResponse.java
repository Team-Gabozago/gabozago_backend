package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileLikesResponse {
    private final String message;
    private final String code;
    private final List<ProfileFeedResponse> likes;

    public ProfileLikesResponse(String message, String code, List<ProfileFeedResponse> likes) {
        this.message = message;
        this.code = code;
        this.likes = likes;
    }

    public static ProfileLikesResponse of(String message, String code, List<ProfileFeedResponse> likes) {
        return new ProfileLikesResponse(message, code, likes);
    }

}
