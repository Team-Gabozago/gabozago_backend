package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Comment;
import com.gabozago.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select distinct com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "where com.feed.id = :feedId and com.parent.id is null " +
            "order by com.id desc")
    List<Comment> findAllByFeedIdAndParentCommentIdIsNull(@Param("feedId") Long feedId);

    @Query("select distinct com " +
            "from Comment as com " +
            "join fetch com.author " +
            "join fetch com.feed " +
            "where com.feed.id = :feedId and com.parent.id = :parentCommentId " +
            "order by com.id desc")
    List<Comment> findAllByFeedIdAndParentCommentId(@Param("feedId") Long feedId, @Param("parentCommentId") Long parentCommentId);

    List<Comment> findAllCommentsByAuthor(User user);
}
