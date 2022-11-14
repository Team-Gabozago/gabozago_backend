package com.gabozago.backend.feed.domain;


import com.gabozago.backend.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Likes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public Like(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }

    public boolean hasFeed(Feed feed) {
        return this.feed.equals(feed);
    }

    public boolean sameAs(User user) {
        return this.user.sameAs(user);
    }
}
