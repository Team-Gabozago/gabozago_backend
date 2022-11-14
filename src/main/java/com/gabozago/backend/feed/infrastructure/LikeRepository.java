package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
