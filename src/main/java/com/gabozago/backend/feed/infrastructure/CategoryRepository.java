package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.feed.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
