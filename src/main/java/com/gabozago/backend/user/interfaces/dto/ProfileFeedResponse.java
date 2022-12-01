package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.category.CategoryResponse;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Location;
import com.gabozago.backend.feed.interfaces.dto.AuthorResponse;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileFeedResponse {
    private final Long id;

    private final AuthorResponse author;

    private final CategoryResponse category;

    private final Location location;

    private final String title;

    private final String content;

    private final int likes;

    private final int comments;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public ProfileFeedResponse(AuthorResponse author, Long id, CategoryResponse category, Location location, String title,
                        String content, int likes, int comments, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.id = id;
        this.category = category;
        this.location = location;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.comments = comments;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProfileFeedResponse of(User author, Feed feed) {
        return new ProfileFeedResponse(AuthorResponse.of(author), feed.getId(), CategoryResponse.of(feed.getCategory()),
                feed.getLocation(),
                feed.getTitle(), feed.getContent(),
                feed.getLikes().size(), feed.getComments().size(), feed.getCreatedAt(), feed.getUpdatedAt());
    }
}
