package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentResponse {
    private final Long id;
    private final String content;
    private final String feedContent;
    private final String parentContent;
    private final boolean feedAuthor;
    private final LocalDateTime updatedAt;
    private final boolean modified;
    private final Long feedId;

    public CommentResponse(Long id, String content, String feedContent, String parentContent, boolean feedAuthor, LocalDateTime updatedAt, boolean modified, Long feedId) {
        this.id = id;
        this.content = content;
        this.feedContent = feedContent;
        this.parentContent = parentContent;
        this.feedAuthor = feedAuthor;
        this.updatedAt = updatedAt;
        this.modified = modified;
        this.feedId = feedId;
    }

    public static CommentResponse of(Comment comment) {
        Comment parent = comment.getParent();
        String parentContent = null;
        if (parent != null) {
            parentContent = parent.getContent();
        }

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getFeed().getContent(),
                parentContent,
                comment.isFeedAuthor(),
                comment.getUpdatedAt(),
                comment.isModified(),
                comment.getFeed().getId()
        );
    }
}
