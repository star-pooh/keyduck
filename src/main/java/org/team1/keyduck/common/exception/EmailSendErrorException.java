package org.team1.keyduck.common.exception;

import lombok.Getter;

@Getter
public class EmailSendErrorException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailSendErrorException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
