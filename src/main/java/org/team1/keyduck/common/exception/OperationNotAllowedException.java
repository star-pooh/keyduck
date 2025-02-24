package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class OperationNotAllowedException extends RuntimeException {

    private final ErrorCode errorCode;

    public OperationNotAllowedException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
