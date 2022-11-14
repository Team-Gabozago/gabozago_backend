package com.gabozago.backend.feed.domain;

import com.gabozago.backend.AbstractEntity;
import com.gabozago.backend.exception.ErrorCode;
import com.gabozago.backend.exception.UnauthorizedException;
import com.gabozago.backend.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent")
    private List<Comment> child = new ArrayList<>();

    public Comment(String content) {
        this(null, content);
    }

    public Comment(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static Comment createReply(String content) {
        return new Comment(content);
    }

    public void update(String content) {
        this.content = content;
    }

//    public void addReply(Comment reply) {
//        this.child.add(reply);
//        reply.parent = this;
//    }

    public Comment writtenBy(User user, Feed feed) {
        this.author = user;
        this.feed = feed;
        feed.addComment(this);
        user.addComment(this);
        return this;
    }

    public boolean isFeedAuthor() {
        return this.author.sameAs(feed.getAuthor());
    }

    public void addChildComment(Comment child) {
        this.child.add(child);
        child.setParent(this);
    }

    public void addParentComment(Comment comment) {
        this.parent = comment;
        comment.addChildComment(this);
    }

    public boolean isParentComment() {
        return Objects.isNull(this.parent);
    }

    public void checkAuthority(User user, ErrorCode errorCode) {
        if (!this.author.sameAs(user)) {
            throw new UnauthorizedException(errorCode);
        }
    }

}
