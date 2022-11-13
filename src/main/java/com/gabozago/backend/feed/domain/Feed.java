package com.gabozago.backend.feed.domain;

import com.gabozago.backend.AbstractEntity;
import com.gabozago.backend.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.*;

@Slf4j
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "feed_id")
    private Long id;

    private String title;

    private String content;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
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
                String title, String content, Address address, int views) {
        this.id = id;
        this.author = author;
        this.category = category;
        this.title = title;
        this.content = content;
        this.address = address;
        this.views = views;
        this.images = new ArrayList<>();
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    // 조회수
//    public void increaseView (boolean alreadyView) {
//        if (alreadyView) {
//            return;
//        }
//        this.views++;
//    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public Feed writtenBy(User author) {
        this.author = author;
        author.addFeed(this);
        return this;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
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
}
