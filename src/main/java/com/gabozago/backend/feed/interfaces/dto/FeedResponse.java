package com.gabozago.backend.feed.interfaces.dto;

import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.entity.User;
import com.gabozago.backend.feed.domain.Location;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class FeedResponse {

    private final AuthorResponse author;

    private final Long id;

    private final Category category;

    private final Location location;

    private final String title;

    private final String content;

    private final int likes;

    private final int views;

    private final boolean liked;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;


    public FeedResponse(AuthorResponse author, Long id, Category category, Location location, String title, String content, int likes, int views,
                        boolean liked, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.id = id;
        this.category = category;
        this.location = location;
        this.title = title;
        this.content = content;
        this.likes = likes;
        this.views = views;
        this.liked = liked;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static FeedResponse of(User author, Feed feed, boolean liked) {
        return new FeedResponse(AuthorResponse.of(author), feed.getId(), feed.getCategory(), feed.getLocation(), feed.getTitle(), feed.getContent(),
                feed.getLikes().size(), feed.getViews(), liked, feed.getCreatedAt(), feed.getUpdatedAt());
    }

    public static List<FeedResponse> toList(List<Feed> feeds) {
        return feeds.stream()
                .map(feed -> of(feed.getAuthor(), feed, false))
                .collect(Collectors.toList());
    }

}
