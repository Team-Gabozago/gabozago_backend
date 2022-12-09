package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Feed;
import com.gabozago.backend.feed.domain.Like;
import com.gabozago.backend.user.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

//    @Query("select distinct feed, ST_Distance_Sphere( POINT(longitude, latitude), POINT(longitude, latitude) ) AS distance " +
//            "(" +
//            "6371 * acos(cos(radians(userLatitude)) " +
//            "* cos(radians(cafeLatitude)) " +
//            "* cos(radians(cafeHardness) - radians(userHardness))" +
//            "+ sin(radians(userLatitude)) *sin(radians(cafeLatitude))" +
//            ")" +
//            ") AS distance " +
//            "from Feed as feed " +
//            "join fetch feed.author " +
//            "where feed.id <= :feedId " +
//            "HAVING diff_Distance <= 0.3 " +
//            "order by feed.createdAt desc")

//    @Query("SELECT p, " +
//            "SQRT(((:a - p.x) * (:a - p.x)) + ((:b - p.y) * (:b - p.y))) as distance from Point p " +
//            "ORDER BY distance ASC")
//    List<Feed> findAll(@Param("feedId") Long feedId, Pageable pageable);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId " +
            "order by feed.updatedAt desc")
    List<Feed> findAllOrderByCreatedAt(@Param("feedId") Long feedId, Pageable pageable);

    @Query("select distinct feed, feed.likes.size as likes " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :feedId " +
            "order by likes desc")
    List<Feed> findAllOrderByLikes(@Param("feedId") Long feedId, Pageable pageable);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "join feed.category fc " +
            "where feed.id <= :nextFeedId " +
            "and fc.name in :categoryNames " +
            "order by feed.updatedAt desc, feed.id desc")
    List<Feed> findByCategoriesOrderByCreatedAt(List<String> categoryNames, Long nextFeedId, Pageable pageable);

    @Query("select distinct feed , feed.likes.size as likes " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "join feed.category fc " +
            "where feed.id <= :nextFeedId " +
            "and fc.name in :categoryNames " +
            "order by likes desc")
    List<Feed> findByCategoriesOrderByLikes(List<String> categoryNames, Long nextFeedId, Pageable pageable);

    List<Feed> findAllByAuthor(User author);

    List<Feed> findAllByLikesIn(List<Like> likes);

    @Query("select distinct feed " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :nextFeedId " +
            "and feed.content like concat('%',:keyword,'%') " +
            "order by feed.createdAt desc, feed.id desc")
    List<Feed> findByKeywordOrderByCreatedAt(@Param("keyword") String keyword, @Param("nextFeedId") Long nextFeedId, Pageable pageable);

    @Query("select distinct feed , feed.likes.size as likes " +
            "from Feed as feed " +
            "join fetch feed.author " +
            "where feed.id <= :nextFeedId " +
            "and feed.content like concat('%',:keyword,'%') " +
            "order by likes desc")
    List<Feed> findByKeywordOrderByLikes(@Param("keyword") String keyword, @Param("nextFeedId") Long nextFeedId, Pageable pageable);

}

