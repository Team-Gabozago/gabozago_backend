package com.gabozago.backend.feed.interfaces.dto;

import com.gabozago.backend.entity.User;
import lombok.Getter;

@Getter
public class AuthorResponse {

    private final Long id;
    private final String nickname;

    // private final String imageUrl;

    public AuthorResponse(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        // this.imageUrl = imageUrl;
    }

    public static AuthorResponse of(User author) {
        return new AuthorResponse(author.getId(), author.getNickName());
    }

}
