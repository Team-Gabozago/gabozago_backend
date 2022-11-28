package com.gabozago.backend.common.exception;

import com.gabozago.backend.common.response.ErrorCode;

public class ConflictException extends BaseException {

    public ConflictException() {
        super(ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public ConflictException(String message) {
        super(message, ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public ConflictException(ErrorCode errorCode) {
        super(errorCode);
    }
}
