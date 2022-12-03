package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetCommentsResponse {
    private final String message;
    private final String code;
    private final List<CommentResponse> comments;

    public GetCommentsResponse(String message, String code, List<CommentResponse> comments) {
        this.message = message;
        this.code = code;
        this.comments = comments;
    }

    public static GetCommentsResponse of(String message, String code, List<CommentResponse> comments) {
        return new GetCommentsResponse(message, code, comments);
    }
}
