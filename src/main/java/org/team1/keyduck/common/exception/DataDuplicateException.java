package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataDuplicateException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataDuplicateException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
