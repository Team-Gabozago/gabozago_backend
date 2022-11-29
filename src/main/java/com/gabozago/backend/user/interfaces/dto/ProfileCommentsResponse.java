package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileCommentsResponse {
    private final String message;
    private final String code;
    private final List<ProfileCommentResponse> comments;

    public ProfileCommentsResponse(String message, String code, List<ProfileCommentResponse> comments) {
        this.message = message;
        this.code = code;
        this.comments = comments;
    }

    public static ProfileCommentsResponse of(String message, String code, List<ProfileCommentResponse> comments) {
        return new ProfileCommentsResponse(message, code, comments);
    }
}
