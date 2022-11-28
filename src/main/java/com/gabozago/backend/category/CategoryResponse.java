package com.gabozago.backend.category;

import com.gabozago.backend.feed.domain.Category;

import lombok.Getter;

@Getter
public class CategoryResponse {

    private final Long id;

    private final String text;

    public CategoryResponse(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    public static CategoryResponse of(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
