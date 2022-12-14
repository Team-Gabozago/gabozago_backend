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
    public List<Feed> searchWithCondition(double userLongitude, double userLatitude, String categories, String keyword, String sortType, Long nextFeedId,
                                          Pageable pageable) {
        List<String> categoryNames = Arrays.asList(categories.split(CATEGORY_SEARCH_DELIMITER));
        if (sortType.equals("NEWEST")) {
            return feedRepository.findByCategoriesOrderByCreatedAt(userLongitude, userLatitude, categoryNames, nextFeedId, pageable);
        }
        return feedRepository.findByCategoriesOrderByLikes(userLongitude, userLatitude, categoryNames, nextFeedId, pageable);
    }
}
