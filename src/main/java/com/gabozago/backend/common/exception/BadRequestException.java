package com.gabozago.backend.common.exception;

import com.gabozago.backend.common.response.ErrorCode;

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
