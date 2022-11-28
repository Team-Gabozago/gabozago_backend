package com.gabozago.backend.feed.interfaces.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.gabozago.backend.feed.domain.Feed;

import lombok.Getter;

@Getter
public class FeedCardResponse {
    private final AuthorResponse author;
    private final Long id;
    private final String title;
    private final String content;
    // private final String thumbnailUrl;

    public FeedCardResponse(AuthorResponse author, Long id, String title, String content) {
        this.author = author;
        this.id = id;
        this.title = title;
        this.content = content;
        // this.thumbnailUrl = thumbnailUrl;
    }

    public static FeedCardResponse of(Feed feed) {
        return new FeedCardResponse(
                AuthorResponse.of(feed.getAuthor()),
                feed.getId(),
                feed.getTitle(),
                feed.getContent()
        // feed.getThumbnailUrl()
        );
    }

    public static List<FeedCardResponse> toList(Collection<Feed> feed) {
        return feed.stream()
                .map(FeedCardResponse::of)
                .collect(Collectors.toList());
    }
}
