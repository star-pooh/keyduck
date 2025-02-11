package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DuplicateDateException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateDateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
