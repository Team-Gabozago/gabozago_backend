package com.gabozago.backend.feed.service;

import com.gabozago.backend.exception.EntityNotFoundException;
import com.gabozago.backend.exception.ErrorCode;
import com.gabozago.backend.exception.NotFoundException;
import com.gabozago.backend.exception.UnauthorizedException;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Location;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FeedService {

    // private final ImageService imageService;

    private final CategoryRepository categoryRepository;

    private final FeedRepository feedRepository;

    public Long create(User user, FeedRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        Feed feed = request.toEntity(user, category);
        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }

    public FeedResponse getFeed(User user, Long feedId) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        return FeedResponse.of(author, feed, liked);
    }

    public void update(User user, Long feedId, FeedRequest request) {
        Feed findFeed = findEntityById(feedId);
        if (findFeed.notSameAuthor(user)) {
            // TODO: 예외처리
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(EntityNotFoundException::new);
        findFeed.update(
                category,
                request.getTitle(),
                request.getContent(),
                new Location(request.getLongitude(), request.getLatitude()));
        // TODO: 이미지 업데이트
    }

    public FeedResponse viewFeed(User user, Long feedId, boolean alreadyView) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        feed.increaseView(alreadyView);
        return FeedResponse.of(author, feed, liked);
    }

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

    public Feed findEntityById(Long feedId) {
        return feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.FEED_NOT_FOUND));
    }
}
