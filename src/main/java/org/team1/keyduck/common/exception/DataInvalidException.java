package org.team1.keyduck.common.exception;

import lombok.Getter;
import org.team1.keyduck.common.util.ErrorCode;

@Getter
public class DataInvalidException extends RuntimeException {

    private final ErrorCode errorCode;

    public DataInvalidException(ErrorCode errorCode, String arg) {
        super(errorCode.getMessage(arg));
        this.errorCode = errorCode;
    }
}
