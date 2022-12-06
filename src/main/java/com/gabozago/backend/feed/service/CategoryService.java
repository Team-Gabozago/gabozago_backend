package com.gabozago.backend.feed.service;


import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

}
