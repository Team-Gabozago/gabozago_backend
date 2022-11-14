package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
