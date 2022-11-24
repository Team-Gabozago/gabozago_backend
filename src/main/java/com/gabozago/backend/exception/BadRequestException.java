package com.gabozago.backend.exception;

public class BadRequestException extends BaseException {

    public BadRequestException() {
        super(ErrorCode.ALREADY_LIKED);
    }

    public BadRequestException(String message) {
        super(message, ErrorCode.ALREADY_LIKED);
    }

    public BadRequestException(ErrorCode errorCode) {
        super(errorCode);
    }

}
