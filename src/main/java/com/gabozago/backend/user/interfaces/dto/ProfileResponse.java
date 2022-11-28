package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.user.domain.Favorite;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final List<ProfileCategoryResponse> categories;

    public ProfileResponse(Long id, String email, String nickname, String profileImage, List<ProfileCategoryResponse> categories) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.categories = categories;
    }

    public static ProfileResponse of(User user, List<Favorite> categories, List<Category> allCategories) {
        String profileImage = null;
        if (user.getProfileImage() != null) {
            profileImage = "http://localhost:8080/profile/images/" + user.getProfileImage().getFileName();
        }

        List<ProfileCategoryResponse> favoriteCategories = allCategories.stream()
                .map(category -> {
                    boolean isFavorite = categories.stream()
                            .anyMatch(favorite -> favorite.getCategory().getId().equals(category.getId()));
                    return ProfileCategoryResponse.of(category, isFavorite);
                })
                .collect(Collectors.toList());


        return new ProfileResponse(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                profileImage,
                favoriteCategories
        );
    }
}