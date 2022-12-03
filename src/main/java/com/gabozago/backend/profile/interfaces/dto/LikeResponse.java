package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LikeResponse {
    private final Long id;
    private final String content;
    private final boolean feedAuthor;
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final Long feedId;

    public LikeResponse(Long id, String content, boolean feedAuthor, LocalDateTime createdAt, boolean modified, Long feedId) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.createdAt = createdAt;
        this.modified = modified;
        this.feedId = feedId;
    }

    public static LikeResponse of(Feed feed, User user) {
        return new LikeResponse(
                feed.getId(),
                feed.getContent(),
                feed.isAuthor(user),
                feed.getCreatedAt(),
                feed.isModified(),
                feed.getId()
        );
    }
}
