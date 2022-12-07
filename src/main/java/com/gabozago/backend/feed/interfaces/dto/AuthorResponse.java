package com.gabozago.backend.feed.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.gabozago.backend.user.domain.User;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AuthorResponse {

    private final Long id;

    private final String nickname;

    private final String profileImageUrl;

    public AuthorResponse(Long id, String nickname, String profileImageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.profileImageUrl = "https://wontu-images.s3.ap-northeast-2.amazonaws.com" + profileImageUrl;
    }

    public static AuthorResponse of(User author) {
        return new AuthorResponse(author.getId(), author.getNickName(), author.getProfileImage().getPath());
    }

}
