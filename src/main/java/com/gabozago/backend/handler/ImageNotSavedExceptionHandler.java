package com.gabozago.backend.handler;

import com.gabozago.backend.common.exception.ConflictException;
import com.gabozago.backend.common.exception.ImageNotSavedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageNotSavedExceptionHandler {

    @ExceptionHandler({ ImageNotSavedException.class })
    public ResponseEntity<Object> handleJsonException(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"message\": \"" + e.getMessage() + "\", \"code\": \"IMAGE_NOT_SAVED\"}");
    }
}
