package com.gabozago.backend.feed.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReplyResponse {

    private final Long id;

    private final String content;

    private final boolean feedAuthor;

    private final LocalDateTime updatedAt;

    private final Long commentId;

    private final AuthorResponse author;

    public ReplyResponse(Long id, String content, boolean feedAuthor, LocalDateTime updatedAt,
            Long commentId, AuthorResponse author) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.updatedAt = updatedAt;
        this.commentId = commentId;
        this.author = author;
    }

    public static ReplyResponse of(Comment reply) {
        return new ReplyResponse(
                reply.getId(),
                reply.getContent(),
                reply.isFeedAuthor(),
                reply.getUpdatedAt(),
                reply.getParent().getId(),
                AuthorResponse.of(reply.getAuthor()));
    }

    public static List<ReplyResponse> toList(List<Comment> replies, User user) {
        return replies.stream()
                .map(reply -> ReplyResponse.of(reply))
                .collect(Collectors.toList());
    }
}
