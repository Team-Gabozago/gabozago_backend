package com.gabozago.backend.feed.interfaces.dto;

import java.util.List;
import java.util.Objects;

import com.gabozago.backend.feed.domain.Feed;

import lombok.Getter;

@Getter
public class FeedCardPaginationResponse {

    private final List<FeedCardResponse> feeds;
    private final Long nextFeedId;

    public FeedCardPaginationResponse(List<FeedCardResponse> feeds, Long nextFeedId) {
        this.feeds = feeds;
        this.nextFeedId = nextFeedId;
    }

    public static FeedCardPaginationResponse of(List<Feed> findFeeds, Feed nextFeed) {
        List<FeedCardResponse> feedCardResponses = FeedCardResponse.toList(findFeeds);
        if (Objects.isNull(nextFeed)) {
            return new FeedCardPaginationResponse(feedCardResponses, null);
        }
        return new FeedCardPaginationResponse(feedCardResponses, nextFeed.getId());
    }
}
