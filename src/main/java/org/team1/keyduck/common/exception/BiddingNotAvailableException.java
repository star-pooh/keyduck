package org.team1.keyduck.common.exception;

public class BiddingNotAvailableException extends RuntimeException {

    private final ErrorCode errorCode;

    public BiddingNotAvailableException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
