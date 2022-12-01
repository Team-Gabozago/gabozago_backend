package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Feed;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileFeedsResponse {
    private String message;
    private String code;
    private List<ProfileFeedResponse> feeds;

    public ProfileFeedsResponse(String message, String code, List<ProfileFeedResponse> feeds) {
        this.message = message;
        this.code = code;
        this.feeds = feeds;
    }

    public static ProfileFeedsResponse of(String message, String code, List<ProfileFeedResponse> feeds) {
        return new ProfileFeedsResponse(message, code, feeds);
    }
}
