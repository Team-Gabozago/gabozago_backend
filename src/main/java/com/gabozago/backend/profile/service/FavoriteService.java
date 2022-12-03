package com.gabozago.backend.profile.service;

import com.gabozago.backend.common.exception.ConflictException;
import com.gabozago.backend.common.exception.EntityNotFoundException;
import com.gabozago.backend.common.response.ErrorCode;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.feed.infrastructure.CategoryRepository;
import com.gabozago.backend.user.domain.User;
import com.gabozago.backend.profile.domain.Favorite;
import com.gabozago.backend.profile.infrastructure.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;

    private final CategoryRepository categoryRepository;

    public List<Favorite> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findAllByUserId(userId);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void addFavorite(User user, Long categoryId) {
        Category category = findCategoryById(categoryId);
        List<Favorite> favorites = favoriteRepository.findAllByUserId(user.getId());

        if (favorites.stream().anyMatch(favorite -> favorite.getCategory().equals(category))) {
            throw new ConflictException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        favoriteRepository.save(new Favorite(user, category));
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_EXISTS));
    }

    public void deleteFavorite(User user, Long categoryId) {
        Category category = findCategoryById(categoryId);
        Favorite favorite = (Favorite) favoriteRepository.findByUserIdAndCategoryId(user.getId(), category.getId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.CATEGORY_NOT_EXISTS));
        favoriteRepository.delete(favorite);
    }
}
