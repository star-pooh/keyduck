package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DataNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
