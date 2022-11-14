package com.gabozago.backend.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ErrorResponse {

    private String message;
    private String code;

    public ErrorResponse(ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
    }

    public String parseJson() {
        return "{ \"message\" : \"" + this.message + "\", \"code\" : \"" + this.code + "\"}";
    }
}
