package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class DuplicateDataException extends RuntimeException {

    private final ErrorCode errorCode;

    public DuplicateDataException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
