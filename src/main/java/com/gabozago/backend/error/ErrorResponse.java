package com.gabozago.backend.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class ErrorResponse {

    private String message;
    private String code;
    private int status;

    public ErrorResponse(ErrorCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus();
        this.code = code.getCode();
    }

    public static ErrorResponse of(ErrorCode code) {
        return new ErrorResponse(code);
    }

    public ResponseEntity<String> entity() {
        return ResponseEntity.status(this.status)
                .body("{ \"message\" : \"" + this.message + "\", \"code\" : \"" + this.code + "\"}");
    }

    public String string() {
        return "{ \"message\" : \"" + this.message + "\", \"code\" : \"" + this.code + "\"}";
    }
}
