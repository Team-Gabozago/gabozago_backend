package com.gabozago.backend.user.service;

import com.gabozago.backend.common.exception.ConflictException;
import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.feed.infrastructure.CommentRepository;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import com.gabozago.backend.feed.infrastructure.LikeRepository;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.user.infrastructure.UserRepository;
import com.gabozago.backend.user.interfaces.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    private final CommentRepository commentRepository;

    private final LikeRepository likeRepository;

    public void update(User user, ProfileUpdateRequest request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new ConflictException("Nickname is already taken");
        }

        user.update(request.getNickname());
        userRepository.save(user);
    }

    public ProfileFeedsResponse getFeedsByUser(User user) {
        List<Feed> feeds = feedRepository.findAllByAuthor(user);

        List<ProfileFeedResponse> profileFeedResponses = feeds.stream()
                .map(feed -> ProfileFeedResponse.of(user, feed))
                .collect(Collectors.toList());

        return ProfileFeedsResponse.of("피드를 성공적으로 조회했습니다", "FEED_FETCHED", profileFeedResponses);
    }

    public void leave(User user) {
        user.leave();
        userRepository.save(user);
    }

    public ProfileCommentsResponse getCommentsByUser(User user) {
        List<Comment> comments = commentRepository.findAllCommentsByAuthor(user);

        List<ProfileCommentResponse> profileCommentResponses = comments.stream()
                .map(ProfileCommentResponse::of)
                .collect(Collectors.toList());

        return ProfileCommentsResponse.of("댓글을 성공적으로 조회했습니다", "COMMENT_FETCHED", profileCommentResponses);
    }


    public ProfileLikesResponse getLikesByUser(User user) {
        List<Like> likes = likeRepository.findAllByUser(user);
        List<Feed> feeds = feedRepository.findAllByLikesIn(likes);

        List<ProfileFeedResponse> profileLikeResponses = feeds.stream()
                .map(feed -> ProfileFeedResponse.of(user, feed))
                .collect(Collectors.toList());

        return ProfileLikesResponse.of("좋아요를 성공적으로 조회했습니다", "LIKE_FETCHED", profileLikeResponses);
    }
}
