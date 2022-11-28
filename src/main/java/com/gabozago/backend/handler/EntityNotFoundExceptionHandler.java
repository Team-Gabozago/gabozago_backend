package com.gabozago.backend.handler;

import com.gabozago.backend.common.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class EntityNotFoundExceptionHandler {
    @ExceptionHandler({ EntityNotFoundException.class })
    public ResponseEntity<Object> handleJsonException(EntityNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("{\"message\": \"" + e.getMessage() + "\", \"code\": \"" + e.getErrorCode() + "\"}");
    }
}
