package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetLikesResponse {
    private final String message;
    private final String code;
    private final List<FeedResponse> likes;

    public GetLikesResponse(String message, String code, List<FeedResponse> likes) {
        this.message = message;
        this.code = code;
        this.likes = likes;
    }

    public static GetLikesResponse of(String message, String code, List<FeedResponse> likes) {
        return new GetLikesResponse(message, code, likes);
    }

}
