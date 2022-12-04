package com.gabozago.backend.feed.service.searchstrategy;


import com.gabozago.backend.feed.infrastructure.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SearchStrategyConfig {

    private final FeedRepository feedRepository;

    @Bean
    public NoneStrategy createNoneStrategy() {
        return new NoneStrategy(feedRepository);
    }

    @Bean
    public CategoriesOnlyStrategy createCategoriesOnlyStrategy() {
        return new CategoriesOnlyStrategy(feedRepository);
    }

}
