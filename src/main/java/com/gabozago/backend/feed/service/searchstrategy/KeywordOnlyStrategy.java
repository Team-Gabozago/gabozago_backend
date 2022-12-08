package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class KeywordOnlyStrategy extends SearchStrategy{

    protected KeywordOnlyStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> searchWithCondition(String categories, String keyword, String sortType, Long nextFeedId, Pageable pageable) {
        if (sortType.equals(FeedSortType.NEWEST)) {
            return feedRepository.findByKeywordOrderByCreatedAt(keyword, nextFeedId, pageable);
        }
        return feedRepository.findByKeywordOrderByLikes(keyword, nextFeedId, pageable);
    }
}

