package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileCommentResponse {
    private final Long id;
    private final String content;
    private final boolean feedAuthor;
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final Long feedId;

    public ProfileCommentResponse(Long id, String content, boolean feedAuthor, LocalDateTime createdAt, boolean modified, Long feedId) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.createdAt = createdAt;
        this.modified = modified;
        this.feedId = feedId;
    }

    public static ProfileCommentResponse of(Comment comment) {
        return new ProfileCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.isFeedAuthor(),
                comment.getCreatedAt(),
                comment.isModified(),
                comment.getFeed().getId()
        );
    }
}
