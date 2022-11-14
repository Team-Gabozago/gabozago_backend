package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Optional<Category> findByCategoryId(@Param("categoryId") Long categoryId);
}
