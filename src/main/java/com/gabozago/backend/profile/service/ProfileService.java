package com.gabozago.backend.profile.service;

import com.gabozago.backend.common.exception.ConflictException;
import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.feed.infrastructure.CommentRepository;
import com.gabozago.backend.feed.infrastructure.FeedRepository;
import com.gabozago.backend.feed.infrastructure.LikeRepository;
import com.gabozago.backend.profile.domain.ProfileImage;
import com.gabozago.backend.profile.interfaces.dto.*;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.profile.infrastructure.ProfileImageRepository;
import com.gabozago.backend.user.infrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final FeedRepository feedRepository;

    private final CommentRepository commentRepository;

    private final LikeRepository likeRepository;
    private final ProfileImageRepository profileImageRepository;

    public void update(User user, UpdateRequest request) {
        if (userRepository.findByNickname(request.getNickname()).isPresent()) {
            throw new ConflictException("Nickname is already taken");
        }

        user.update(request.getNickname());
        userRepository.save(user);
    }

    public GetFeedsResponse getFeedsByUser(User user) {
        List<Feed> feeds = feedRepository.findAllByAuthor(user);

        List<FeedResponse> profileFeedResponses = feeds.stream()
                .map(feed -> FeedResponse.of(user, feed))
                .collect(Collectors.toList());

        return GetFeedsResponse.of("피드를 성공적으로 조회했습니다", "FEED_FETCHED", profileFeedResponses);
    }

    public void leave(User user) {
        user.leave();
        userRepository.save(user);
    }

    public GetCommentsResponse getCommentsByUser(User user) {
        List<Comment> comments = commentRepository.findAllCommentsByAuthor(user);

        List<CommentResponse> profileCommentResponses = comments.stream()
                .map(CommentResponse::of)
                .collect(Collectors.toList());

        return GetCommentsResponse.of("댓글을 성공적으로 조회했습니다", "COMMENT_FETCHED", profileCommentResponses);
    }


    public GetLikesResponse getLikesByUser(User user) {
        List<Like> likes = likeRepository.findAllByUser(user);
        List<Feed> feeds = feedRepository.findAllByLikesIn(likes);

        List<FeedResponse> profileLikeResponses = feeds.stream()
                .map(feed -> FeedResponse.of(user, feed))
                .collect(Collectors.toList());

        return GetLikesResponse.of("좋아요를 성공적으로 조회했습니다", "LIKE_FETCHED", profileLikeResponses);
    }

    public UploadProfileImageResponse saveProfileImage(ProfileImage profileImage, User user) {
        profileImageRepository.save(profileImage);
        user.updateProfileImage(profileImage);
        userRepository.save(user);

        return UploadProfileImageResponse.of(profileImage);
    }
}
