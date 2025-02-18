package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataNotValidException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataNotValidException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}

