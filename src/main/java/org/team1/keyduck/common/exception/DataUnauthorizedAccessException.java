package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataUnauthorizedAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataUnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
