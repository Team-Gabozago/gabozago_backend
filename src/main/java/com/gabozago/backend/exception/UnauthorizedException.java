package com.gabozago.backend.exception;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super(ErrorCode.UNAUTHORIZED_UPDATE_COMMENT);
    }

    public UnauthorizedException(ErrorCode errorCode) {
        super(errorCode);
    }

    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED_UPDATE_COMMENT);
    }

}
