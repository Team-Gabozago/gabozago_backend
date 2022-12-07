package com.gabozago.backend.feed.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.category.CategoryResponse;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.feed.domain.Location;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FeedResponse {

    private final AuthorResponse author;

    private final Long id;

    private final CategoryResponse category;

    private final Location location;

    private final String title;

    private final String content;

    private final int likes;

    private final boolean liked;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public FeedResponse(AuthorResponse author, Long id, CategoryResponse category, Location location, String title,
            String content, int likes,
            boolean liked, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.id = id;
        this.category = category;
        this.location = location;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.liked = liked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FeedResponse of(User author, Feed feed, boolean liked) {
        return new FeedResponse(AuthorResponse.of(author), feed.getId(), CategoryResponse.of(feed.getCategory()),
                feed.getLocation(),
                feed.getTitle(), feed.getContent(),
                feed.getLikes().size(), liked, feed.getCreatedAt(), feed.getUpdatedAt());
    }

}
