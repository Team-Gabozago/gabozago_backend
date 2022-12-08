package com.gabozago.backend.feed.service;

import com.gabozago.backend.common.exception.EntityNotFoundException;
import com.gabozago.backend.common.exception.NotFoundException;
import com.gabozago.backend.common.exception.UnauthorizedException;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Image;
import com.gabozago.backend.feed.domain.Location;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import com.gabozago.backend.feed.interfaces.dto.FeedCardPaginationResponse;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.feed.service.searchstrategy.SearchStrategy;
import com.gabozago.backend.feed.service.searchstrategy.SearchStrategyFactory;
import com.gabozago.backend.image.service.ImageRepository;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedService {

    private static final int NEXT_FEED_COUNT = 1;

    private final CategoryRepository categoryRepository;

    private final FeedRepository feedRepository;

    private final ImageRepository imageRepository;

    @Transactional
    public Long create(User user, FeedRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        Feed feed = request.toEntity(category).writtenBy(user);
        Feed savedFeed = feedRepository.save(feed);

        // 이미지 처리 임시
        request.getImages().stream().forEach(imageUrl -> {
            Image image = Image.builder()
                    .filePath(imageUrl)
                    .build().writtenBy(savedFeed);
            imageRepository.save(image);
        });
        return savedFeed.getId();
    }

    @Transactional
    public void update(User user, Long feedId, FeedRequest request) {
        Feed findFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FEED_NOT_FOUND));
        if (findFeed.notSameAuthor(user)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_UPDATE_FEED);
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        findFeed.update(
                category,
                request.getTitle(),
                request.getContent(),
                new Location(request.getLongitude(), request.getLatitude(), request.getPlace(),
                        request.getPlaceDetail()));

        // TODO: 이미지 업데이트
    }

    @Transactional
    public FeedResponse viewFeed(User user, Long feedId) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        return FeedResponse.of(author, feed, liked);
    }

    @Transactional
    public void delete(User user, Long feedId) {
        Feed findFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.FEED_NOT_FOUND));
        if (findFeed.notSameAuthor(user)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED_DELETE_FEED);
        }

        // TODO 알림
        // applicationEventPublisher.publishEvent();

        feedRepository.delete(findFeed);
    }

    @Transactional(readOnly = true)
    public Feed findEntityById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FEED_NOT_FOUND));
    }

    @Transactional
    public FeedCardPaginationResponse generateFeedCardPaginationResponse(int countPerPage, List<Feed> findFeeds) {
        if (findFeeds.size() == countPerPage + NEXT_FEED_COUNT) {
            Feed nextFeed = findFeeds.get(countPerPage);
            findFeeds.remove(nextFeed);
            return FeedCardPaginationResponse.of(findFeeds, nextFeed);
        }
        return FeedCardPaginationResponse.of(findFeeds, null);
    }

    @Transactional(readOnly = true)
    public FeedCardPaginationResponse findRecentFeeds(String categories, String keyword, long nextFeedId,
            int countPerPage,
            String sortType) {
        Pageable pageable = PageRequest.of(0, countPerPage + NEXT_FEED_COUNT);
        SearchStrategy searchStrategy = SearchStrategyFactory.of(categories, keyword).findStrategy();
        List<Feed> findFeeds = searchStrategy.searchWithCondition(categories, keyword, sortType, nextFeedId, pageable);
        return generateFeedCardPaginationResponse(countPerPage, findFeeds);
    }

}
