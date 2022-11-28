package com.gabozago.backend.feed.service;

import com.gabozago.backend.common.exception.BadRequestException;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.feed.infrastructure.LikeRepository;

import lombok.AllArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final FeedService feedService;

    private final ApplicationEventPublisher applicationEventPublisher;

    public void addLike(User user, Long feedId) {
        Feed findFeed = feedService.findEntityById(feedId);
        if (user.isLiked(findFeed)) {
            throw new BadRequestException(ErrorCode.ALREADY_LIKED);
        }
        user.addLike(new Like(user, findFeed));

        // TODO: 알림
        // applicationEventPublisher.publishEvent();
    }

    public void deleteLike(User user, Long feedId) {
        Feed findFeed = feedService.findEntityById(feedId);
        Like findLike = findFeed.findLikeBy(user)
                .orElseThrow(() -> new BadRequestException(ErrorCode.NOT_LIKED));
        likeRepository.delete(findLike);
        findFeed.delete(findLike);
        user.delete(findLike);
    }
}
