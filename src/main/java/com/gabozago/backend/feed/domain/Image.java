package com.gabozago.backend.feed.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id", nullable = false)
    private Long id;

    private String filePath;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    private String state;

    @Builder
    public Image(Long id, String filePath, String state) {
        this.id = id;
        this.filePath = filePath;
        this.state = state;
    }

    public void update(String filePath) {
        this.filePath = filePath;
    }

    public void delete() {
        this.state = "INACTIVE";
    }

    public Image writtenBy(Feed feed) {
        this.feed = feed;
        feed.addImage(this);
        return this;
    }
}
