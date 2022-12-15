package com.gabozago.backend.feed.infrastructure;

import com.gabozago.backend.user.infrastructure.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FeedRepositoryTest {
    @Autowired
    private FeedRepository feedRepository;

    @Test
    void testFindAllOrderByCreatedAt() {
        feedRepository.findAllOrderByCreatedAt(1, 1, 15L, PageRequest.of(0, 16));
    }
}