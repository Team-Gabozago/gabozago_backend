package com.gabozago.backend.fixture;

import com.gabozago.backend.feed.domain.Category;

public class CategoryFixture {

    private CategoryFixture() {

    }

    public static Category 테니스_생성() {
        return new Category("테니스");
    }

    public static Category 축구_생성() {
        return new Category("축구");
    }

    public static Category 농구_생성() {
        return new Category("농구");
    }

}
