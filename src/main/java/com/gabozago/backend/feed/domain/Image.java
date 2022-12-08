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

    @Builder
    public Image(Long id, String filePath) {
        this.id = id;
        this.filePath = filePath;
    }

    public Image writtenBy(Feed feed) {
        this.feed = feed;
        feed.addImage(this);
        return this;
    }
}
