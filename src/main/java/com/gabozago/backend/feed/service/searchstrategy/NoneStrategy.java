package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class NoneStrategy extends SearchStrategy {

    protected NoneStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> searchWithCondition(double userLongitude, double userLatitude, String categories, String keyword, String sortType, Long nextFeedId, Pageable pageable) {
        if (sortType.equals("NEWEST")) {
            return feedRepository.findAllOrderByCreatedAt(userLongitude, userLatitude, nextFeedId, pageable);
        }
        return feedRepository.findAllOrderByLikes(userLongitude, userLatitude, nextFeedId, pageable);
    }
}
