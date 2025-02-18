package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataInvalidException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataInvalidException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}

