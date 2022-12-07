package com.gabozago.backend.feed.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Comment;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CommentResponse {

    private final Long id;

    private final String content;

    private final boolean feedAuthor;

    private final LocalDateTime updatedAt;

    private final AuthorResponse author;

    public CommentResponse(Long id, String content, boolean feedAuthor, LocalDateTime updatedAt,
            AuthorResponse author) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.updatedAt = updatedAt;
        this.author = author;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.isFeedAuthor(),
                comment.getUpdatedAt(),
                AuthorResponse.of(comment.getAuthor()));
    }

    public static List<CommentResponse> toList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.of(comment))
                .collect(Collectors.toList());
    }

}
