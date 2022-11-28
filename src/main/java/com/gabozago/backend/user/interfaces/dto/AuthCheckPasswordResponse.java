package com.gabozago.backend.user.interfaces.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthCheckPasswordResponse {
    private final String message;
    private final boolean isCorrect;

    public AuthCheckPasswordResponse(String message, boolean isCorrect) {
        this.message = message;
        this.isCorrect = isCorrect;
    }

    public static AuthCheckPasswordResponse of(String message, boolean isCorrect) {
        return new AuthCheckPasswordResponse(message, isCorrect);
    }
}
