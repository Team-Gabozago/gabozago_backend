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
@Table(name="feed")
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

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @Builder
    public Feed(Long id, Category category,
            String title, String content, Location location, User author) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.location = location;
        this.author = author;
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

    public void addImage(Image image) {
        this.images.add(image);
    }

    public void deleteImage(Image image) {
        this.images.remove(image);
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

    public boolean isAuthor(User user) {
        return this.author.sameAs(user);
    }
}
