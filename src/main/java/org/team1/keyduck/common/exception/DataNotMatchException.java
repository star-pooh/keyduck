package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataNotMatchException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataNotMatchException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
