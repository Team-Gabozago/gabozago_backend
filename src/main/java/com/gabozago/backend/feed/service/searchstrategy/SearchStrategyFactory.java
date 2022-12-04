package com.gabozago.backend.feed.service.searchstrategy;

import com.gabozago.backend.common.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

public enum SearchStrategyFactory {

    NONE(true),

    CATEGORIES_ONLY(false);

    private final boolean isCategoriesEmpty;

    private SearchStrategy searchStrategy;

    SearchStrategyFactory(boolean isCategoriesEmpty) {
        this.isCategoriesEmpty = isCategoriesEmpty;
    }

    public static SearchStrategyFactory of(String categories) {
        return Arrays.stream(values())
                .filter(searchStrategyFactory -> searchStrategyFactory.isCategoriesEmpty == categories.isEmpty())
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("asdasdasd"));
    }

    private void setSearchStrategy(SearchStrategy searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public SearchStrategy findStrategy() {
        return this.searchStrategy;
    }


    @Component
    @AllArgsConstructor
    private static class StrategyInjector {
        private NoneStrategy noneStrategy;
        private CategoriesOnlyStrategy categoriesOnlyStrategy;

        @PostConstruct
        private void inject() {
            NONE.setSearchStrategy(noneStrategy);
            CATEGORIES_ONLY.setSearchStrategy(categoriesOnlyStrategy);
        }
    }

}
