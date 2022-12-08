package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public abstract class SearchStrategy {

    protected static final String CATEGORY_SEARCH_DELIMITER = ",";

    protected final FeedRepository feedRepository;

    protected SearchStrategy(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public abstract List<Feed> searchWithCondition(String categories, String keyword, String sortType, Long nextFeedId, Pageable pageable);
}
