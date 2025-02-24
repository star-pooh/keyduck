package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataUnauthorizedAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataUnauthorizedAccessException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
