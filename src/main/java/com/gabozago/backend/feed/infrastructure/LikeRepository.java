package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByUser(User user);
}
