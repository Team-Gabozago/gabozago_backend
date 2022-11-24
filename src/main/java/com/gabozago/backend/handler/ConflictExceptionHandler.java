package com.gabozago.backend.handler;

import com.gabozago.backend.exception.ConflictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ConflictExceptionHandler {

    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<Object> handleJsonException(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("{\"message\": \"" + e.getMessage() + "\", \"code\": \"" + e.getErrorCode() + "\"}");
    }
}
