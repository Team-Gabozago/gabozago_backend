package com.gabozago.backend.feed.domain;

import com.gabozago.backend.user.domain.Favorite;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "category_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<Favorite> users = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void addUser(Favorite favorite) {
        users.add(favorite);
    }
}
