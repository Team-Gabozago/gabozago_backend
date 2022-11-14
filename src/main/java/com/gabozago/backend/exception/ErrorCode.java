package com.gabozago.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황

    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),

    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),

    COMMON_ILLEGAL_STATUS("잘못된 상태값입니다."),

    NOT_FOUND("해당 url을 찾을 수 없습니다."),

    FEED_NOT_FOUND("존재하지 않는 피드입니다."),

    UNAUTHORIZED_UPDATE_FEED("피드는 작성자만 수정할 수 있습니다."),

    UNAUTHORIZED_DELETE_FEED("피드는 작성자만 삭제할 수 있습니다."),

    COMMENT_NOT_FOUND("존재하지 않는 댓글입니다."),

    UNAUTHORIZED_UPDATE_COMMENT("댓글은 작성자만 수정할 수 있습니다."),

    UNAUTHORIZED_DELETE_COMMENT("댓글은 작성자만 삭제할 수 있습니다."),

    ALREADY_LIKED("이미 좋아요 누른 글 입니다."),

    NOT_LIKED("좋아요를 누르지 않았습니다.");

    private final String errorMsg;

    public String getErrorMsg(Object... arg) {
        return String.format(errorMsg, arg);
    }

}
