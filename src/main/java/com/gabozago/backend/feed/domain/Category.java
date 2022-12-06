package com.gabozago.backend.feed.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabozago.backend.profile.domain.Favorite;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Feed> feeds = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "category")
    private List<Favorite> users = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public void addUser(Favorite favorite) {
        users.add(favorite);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id) && Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
