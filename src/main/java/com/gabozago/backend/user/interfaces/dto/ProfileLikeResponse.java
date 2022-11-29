package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileLikeResponse {
    private final Long id;
    private final String content;
    private final boolean feedAuthor;
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final Long feedId;

    public ProfileLikeResponse(Long id, String content, boolean feedAuthor, LocalDateTime createdAt, boolean modified, Long feedId) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.createdAt = createdAt;
        this.modified = modified;
        this.feedId = feedId;
    }

    public static ProfileLikeResponse of(Feed feed, User user) {
        return new ProfileLikeResponse(
                feed.getId(),
                feed.getContent(),
                feed.isAuthor(user),
                feed.getCreatedAt(),
                feed.isModified(),
                feed.getId()
        );
    }
}
