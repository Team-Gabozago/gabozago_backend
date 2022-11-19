package com.gabozago.backend.exception;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException() {
        super(ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public EntityNotFoundException(String message) {
        super(message, ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public EntityNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }


}
