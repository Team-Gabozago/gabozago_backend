package com.gabozago.backend.exception;

public class NotFoundException extends BaseException {


    public NotFoundException() {
        super(ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public NotFoundException(String message) {
        super(message, ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
