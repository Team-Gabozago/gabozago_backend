package com.gabozago.backend.profile.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.feed.domain.Category;
import com.gabozago.backend.profile.domain.Favorite;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FindResponse {
    private final String message;
    private final String code;
    private final Long id;
    private final String email;
    private final String nickname;
    private final String profileImage;
    private final List<CategoryResponse> categories;

    public FindResponse(Long id, String email, String nickname, String profileImage, List<CategoryResponse> categories) {
        this.message = "프로필 정보를 가져왔습니다.";
        this.code = "PROFILE_FETCHED";
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
        this.categories = categories;
    }

    public static FindResponse of(User user, List<Favorite> categories, List<Category> allCategories) {
        String profileImage = null;
        if (user.getProfileImage() != null) {
            profileImage = "https://wontu-images.s3.ap-northeast-2.amazonaws.com" + user.getProfileImage().getPath();
        } else {
            profileImage = "https://res.cloudinary.com/dseikbqa7/image/upload/v1668526362/wontu/gopher_a3mcbl.png";
        }

        List<CategoryResponse> favoriteCategories = allCategories.stream()
                .map(category -> {
                    boolean isFavorite = categories.stream()
                            .anyMatch(favorite -> favorite.getCategory().getId().equals(category.getId()));
                    return CategoryResponse.of(category, isFavorite);
                })
                .collect(Collectors.toList());


        return new FindResponse(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                profileImage,
                favoriteCategories
        );
    }
}
