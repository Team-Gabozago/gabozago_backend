package com.gabozago.backend.feed.service;

import com.gabozago.backend.exception.ErrorCode;
import com.gabozago.backend.exception.NotFoundException;
import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.infrastructure.CommentRepository;
import com.gabozago.backend.feed.interfaces.dto.CommentRequest;
import com.gabozago.backend.feed.interfaces.dto.CommentResponse;
import com.gabozago.backend.feed.interfaces.dto.ReplyResponse;
import com.gabozago.backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final FeedService feedService;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * Comment - create
     *
     * @param user
     * @param feedId
     * @param request
     * @return
     */
    public CommentResponse createComment(User user, Long feedId, CommentRequest request) {
        Feed feed = feedService.findEntityById(feedId);
        Comment comment = new Comment(request.getContent()).writtenBy(user, feed);
        commentRepository.save(comment);

        // TODO 알림
        // applicationEventPublisher.publishEvent();
        return CommentResponse.of(comment);
    }

    /**
     * Comment - update
     *
     * @param commentId
     * @param request
     * @param user
     * @return
     */
    public CommentResponse updateComment(Long commentId, CommentRequest request, User user) {
        Comment findComment = findEntityById(commentId);
        findComment.checkAuthority(user, ErrorCode.UNAUTHORIZED_UPDATE_COMMENT);

        // TODO 알림
        // applicationEventPublisher.publishEvent();

        findComment.update(request.getContent());
        Comment updatedComment = commentRepository.saveAndFlush(findComment);
        return CommentResponse.of(updatedComment);
    }

    /**
     * Comment - delete
     *
     * @param user
     * @param commentId
     */
    public void deleteComment(User user, Long commentId) {
        Comment findComment = findEntityById(commentId);
        findComment.checkAuthority(user, ErrorCode.UNAUTHORIZED_DELETE_COMMENT);

        // TODO 알림
        // applicationEventPublisher.publishEvent();
        user.deleteComment(findComment);
    }

    /**
     * Reply - create
     *
     * @param user
     * @param feedId
     * @param commentId
     * @param request
     * @return
     */
    public CommentResponse createReply(User user, Long feedId, Long commentId, CommentRequest request) {
        Feed findFeed = feedService.findEntityById(feedId);
        Comment reply = Comment.createReply(request.getContent()).writtenBy(user, findFeed);
        Comment comment = findEntityById(commentId);
        reply.addParentComment(comment);
        Comment saveReply = commentRepository.save(reply);

        // TODO 알림
        // applicationEventPublisher.publishEvent();
        return CommentResponse.of(saveReply);
    }

    /**
     * Comment - read (1)
     *
     * @param commentId
     * @return
     */
    @Transactional(readOnly = true)
    public Comment findEntityById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    /**
     * Comment - read (2)
     *
     * @param feedId
     * @param user
     * @return
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByFeedId(Long feedId, User user) {
        List<Comment> comments = commentRepository.findAllByFeedIdAndParentCommentIdIsNull(feedId);
        return CommentResponse.toList(comments);
    }

    /**
     * Reply - read
     *
     * @param user
     * @param feedId
     * @param commentId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ReplyResponse> findAllRepliesById(User user, Long feedId, Long commentId) {
        List<Comment> replies = commentRepository.findAllByFeedIdAndParentCommentId(feedId, commentId);
        return ReplyResponse.toList(replies, user);
    }
}
