package com.gabozago.backend.feed.interfaces.dto;

import com.gabozago.backend.user.domain.User;
import lombok.Getter;

@Getter
public class AuthorResponse {

    private final Long id;

    private final String nickname;

    private final String path;

    public AuthorResponse(Long id, String nickname, String path) {
        this.id = id;
        this.nickname = nickname;
         this.path = "https://wontu-images.s3.ap-northeast-2.amazonaws.com" + path;
    }

    public static AuthorResponse of(User author) {
        return new AuthorResponse(author.getId(), author.getNickName(), author.getProfileImage().getPath());
    }

}
