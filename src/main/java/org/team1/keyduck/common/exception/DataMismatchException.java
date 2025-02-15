package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataMismatchException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataMismatchException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
