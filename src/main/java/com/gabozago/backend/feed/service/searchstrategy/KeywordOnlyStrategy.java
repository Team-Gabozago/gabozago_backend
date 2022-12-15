package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class KeywordOnlyStrategy extends SearchStrategy {

    protected KeywordOnlyStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> searchWithCondition(double userLongitude, double userLatitude, String categories, String keyword, String sortType, Long nextFeedId, Pageable pageable) {
        if (sortType.equals(FeedSortType.NEWEST)) {
            return feedRepository.findByKeywordOrderByCreatedAt(userLongitude, userLatitude, keyword, nextFeedId, pageable);
        }
        return feedRepository.findByKeywordOrderByLikes(userLongitude, userLatitude, keyword, nextFeedId, pageable);
    }
}

