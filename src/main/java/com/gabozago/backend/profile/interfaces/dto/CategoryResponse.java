package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Category;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CategoryResponse {
    private final Long id;
    private final String name;

    private final boolean isFavorite;

    public CategoryResponse(Long id, String name, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public static CategoryResponse of(Category category, boolean isFavorite) {
        return new CategoryResponse(category.getId(), category.getName(), isFavorite);
    }
}
