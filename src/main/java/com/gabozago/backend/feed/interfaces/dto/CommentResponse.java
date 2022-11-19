package com.gabozago.backend.feed.interfaces.dto;

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
    private final LocalDateTime createdAt;
    private final boolean modified;
    private final AuthorResponse author;

    public CommentResponse(Long id, String content, boolean feedAuthor, LocalDateTime createdAt, boolean modified, AuthorResponse author) {
        this.id = id;
        this.content = content;
        this.feedAuthor = feedAuthor;
        this.createdAt = createdAt;
        this.modified = modified;
        this.author = author;
    }

    public static CommentResponse of(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.isFeedAuthor(),
                comment.getCreatedAt(),
                comment.isModified(),
                AuthorResponse.of(comment.getAuthor())
        );
    }

    public static List<CommentResponse> toList(List<Comment> comments) {
        return comments.stream()
                .map(comment -> CommentResponse.of(comment))
                .collect(Collectors.toList());
    }

}
