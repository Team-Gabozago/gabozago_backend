package com.gabozago.backend.feed.service;

import com.gabozago.backend.exception.EntityNotFoundException;
import com.gabozago.backend.exception.ErrorCode;
import com.gabozago.backend.exception.NotFoundException;
import com.gabozago.backend.exception.UnauthorizedException;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Location;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import com.gabozago.backend.feed.interfaces.dto.FeedRequest;
import com.gabozago.backend.feed.interfaces.dto.FeedResponse;
import com.gabozago.backend.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FeedService {

    // private final ImageService imageService;

    private final CategoryRepository categoryRepository;

    private final FeedRepository feedRepository;

    public Long create(User user, FeedRequest request) {
        Feed feed = request.toEntity().writtenBy(user);
        feed.setCategory(
                categoryRepository.findById(request.getCategoryId()).orElseThrow(EntityNotFoundException::new));

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
        Feed feed = user.findMyFeed(feedId);

        feed.update(
            request.getTitle(),
            request.getContent()
        );

        // TODO 이미지

        feed.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow(EntityNotFoundException::new));
        feed.setLocation(new Location(request.getLongitude(), request.getLatitude()));
    }

    public FeedResponse viewFeed(User user, Long feedId, boolean alreadyView) {
        Feed feed = findEntityById(feedId);
        User author = feed.getAuthor();
        boolean liked = user.isLiked(feed);
        feed.increaseView(alreadyView);
        return FeedResponse.of(author, feed, liked);
    }

    public void delete(User user, Long feedId) {
        Feed findFeed  = feedRepository.findById(feedId)
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
}
