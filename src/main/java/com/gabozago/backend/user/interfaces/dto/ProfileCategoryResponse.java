package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Category;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileCategoryResponse {
    private final Long id;
    private final String name;

    private final boolean isFavorite;

    public ProfileCategoryResponse(Long id, String name, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.isFavorite = isFavorite;
    }

    public static ProfileCategoryResponse of(Category category, boolean isFavorite) {
        return new ProfileCategoryResponse(category.getId(), category.getName(), isFavorite);
    }
}
