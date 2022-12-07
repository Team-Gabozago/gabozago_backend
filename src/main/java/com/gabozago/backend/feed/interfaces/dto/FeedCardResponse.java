package com.gabozago.backend.feed.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Feed;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FeedCardResponse {

    private final AuthorResponse author;

    private final Long id;

    private final String title;

    private final String content;

    // private final String thumbnailUrl;

    private final int likes;

    private final int comments;

    private final LocalDateTime updatedAt;

    public FeedCardResponse(AuthorResponse author, Long id, String title, String content, int likes, int comments,
            LocalDateTime updatedAt) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.content = content;
        // this.thumbnailUrl = thumbnailUrl;
        this.likes = likes;
        this.comments = comments;
        this.updatedAt = updatedAt;
    }

    public static FeedCardResponse of(Feed feed) {
        return new FeedCardResponse(
                AuthorResponse.of(feed.getAuthor()),
                feed.getId(),
                feed.getTitle(),
                feed.getContent(),
                feed.getLikes().size(),
                feed.getComments().size(),
                feed.getUpdatedAt()
        // feed.getThumbnailUrl()
        );
    }

    public static List<FeedCardResponse> toList(Collection<Feed> feed) {
        return feed.stream()
                .map(FeedCardResponse::of)
                .collect(Collectors.toList());
    }
}
