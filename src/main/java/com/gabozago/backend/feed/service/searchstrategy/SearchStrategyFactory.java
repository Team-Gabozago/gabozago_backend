package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.common.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public enum SearchStrategyFactory {

    NONE(true, true),

    CATEGORIES_ONLY(false, true),

    KEYWORD_ONLY(true, false);

    private final boolean isCategoriesEmpty;
    private final boolean isKeywordEmpty;

    private SearchStrategy searchStrategy;

    SearchStrategyFactory(boolean isCategoriesEmpty, boolean isKeywordEmpty) {
        this.isCategoriesEmpty = isCategoriesEmpty;
        this.isKeywordEmpty = isKeywordEmpty;
    }

    public static SearchStrategyFactory of(String categories, String keyword) {
        return Arrays.stream(values())
                .filter(searchStrategyFactory -> searchStrategyFactory.isCategoriesEmpty == categories.isEmpty())
                .filter(searchStrategyFactory -> searchStrategyFactory.isKeywordEmpty == keyword.isEmpty())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("asdasdasd"));
    }

    private void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchStrategy findStrategy() { return this.searchStrategy; }


    @Component
    @AllArgsConstructor
    private static class StrategyInjector {
        private NoneStrategy noneStrategy;
        private CategoriesOnlyStrategy categoriesOnlyStrategy;

        private KeywordOnlyStrategy keywordOnlyStrategy;

        @PostConstruct
        private void inject() {
            NONE.setSearchStrategy(noneStrategy);
            CATEGORIES_ONLY.setSearchStrategy(categoriesOnlyStrategy);
            KEYWORD_ONLY.setSearchStrategy(keywordOnlyStrategy);
        }
    }

}
