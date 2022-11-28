package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Feed;
import java.util.List;
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
            "order by feed.createdDate desc, feed.id desc")
    List<Feed> findAllFeed(@Param("feedId") Long feedId, Pageable pageable);

}
