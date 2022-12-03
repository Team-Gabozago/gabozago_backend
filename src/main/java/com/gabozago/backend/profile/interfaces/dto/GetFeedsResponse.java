package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GetFeedsResponse {
    private String message;
    private String code;
    private List<FeedResponse> feeds;

    public GetFeedsResponse(String message, String code, List<FeedResponse> feeds) {
        this.message = message;
        this.code = code;
        this.feeds = feeds;
    }

    public static GetFeedsResponse of(String message, String code, List<FeedResponse> feeds) {
        return new GetFeedsResponse(message, code, feeds);
    }
}
