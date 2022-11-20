package com.gabozago.backend.feed.domain;

import com.gabozago.backend.AbstractEntity;
import com.gabozago.backend.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "feed_id")
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @Embedded
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "feed")
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "feed")
    private List<Like> likes = new ArrayList<>();

    private int views;

    @Builder
    public Feed(Long id, User author, Category category,
            String title, String content, Location location, int views) {
        this.id = id;
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.location = location;
        this.views = views;
        this.images = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Feed writtenBy(User author) {
        this.author = author;
        author.addFeed(this);
        return this;
    }

    public void update(Category category, String title, String content, Location location) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.location = location;
    }

    public boolean notSameAuthor(User user) {
        return !author.sameAs(user);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void deleteComment(Comment comment) {
        this.comments.remove(comment);
    }

    public int likesCount() {
        return likes.size();
    }

    public void addLike(Like like) {
        this.likes.add(like);
    }

    public void removeLike(Like like) {
        this.likes.remove(like);
    }

    public Optional<Like> findLikeBy(User user) {
        return this.likes.stream()
                .filter(like -> like.sameAs(user))
                .findAny();
    }

    public Map<Comment, List<Comment>> mapByCommentAndReplies() {
        Map<Comment, List<Comment>> commentAndReplies = new HashMap<>();

        for (Comment comment : comments) {
            if (comment.isParentComment()) {
                commentAndReplies.put(comment, comment.getChild());
            }
        }
        return commentAndReplies;
    }

    public void increaseView(boolean alreadyView) {
        if (alreadyView) {
            return;
        }
        this.views++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Feed feed = (Feed) o;
        return Objects.equals(id, feed.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
