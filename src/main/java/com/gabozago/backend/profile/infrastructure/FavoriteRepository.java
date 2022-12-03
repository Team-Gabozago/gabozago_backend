package com.gabozago.backend.profile.infrastructure;

import com.gabozago.backend.profile.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findAllByUserId(Long userId);

    Optional<Object> findByUserIdAndCategoryId(Long userId, Long categoryId);
}
