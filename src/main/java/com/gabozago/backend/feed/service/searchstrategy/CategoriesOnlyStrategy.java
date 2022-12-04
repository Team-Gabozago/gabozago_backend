package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;

public class CategoriesOnlyStrategy extends SearchStrategy {

    protected CategoriesOnlyStrategy(FeedRepository feedRepository) {
        super(feedRepository);
    }

    @Override
    public List<Feed> searchWithCondition(String categories, String sortType, Long nextFeedId, Pageable pageable) {
        List<String> categoryNames = Arrays.asList(categories.split(CATEGORY_SEARCH_DELIMITER));
        if (sortType.equals(FeedSortType.NEWEST)) {
            return feedRepository.findByCategoriesOrderByCreatedAt(categoryNames, nextFeedId, pageable);
        }
        return feedRepository.findByCategoriesOrderByLikes(categoryNames, nextFeedId, pageable);
    }
}
