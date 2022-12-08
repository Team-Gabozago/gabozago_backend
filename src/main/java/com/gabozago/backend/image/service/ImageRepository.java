package com.gabozago.backend.image.service;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByFeed(Feed feed);

}
