package com.gabozago.backend.feed.interfaces.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class CommentRequest {

//    @NotBlank
    private String content;

    public CommentRequest(String content) {
        this.content = content;
    }

}
