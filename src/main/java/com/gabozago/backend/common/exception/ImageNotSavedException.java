package com.gabozago.backend.common.exception;

import com.gabozago.backend.common.response.ErrorCode;

public class ImageNotSavedException extends BaseException {
    public ImageNotSavedException() {
        super(ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public ImageNotSavedException(String message) {
        super(message, ErrorCode.COMMON_INVALID_PARAMETER);
    }

    public ImageNotSavedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
