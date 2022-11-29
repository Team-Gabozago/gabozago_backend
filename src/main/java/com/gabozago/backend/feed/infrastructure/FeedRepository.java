package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Feed;

import java.util.Collection;
import java.util.List;

import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId " +
            "order by feed.createdAt desc, feed.id desc")
    List<Feed> findAllFeed(@Param("feedId") Long feedId, Pageable pageable);

    List<Feed> findAllByAuthor(User author);

    List<Feed> findAllByLikesIn(List<Like> likes);
}
