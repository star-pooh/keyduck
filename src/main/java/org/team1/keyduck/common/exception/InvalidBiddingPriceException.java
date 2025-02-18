package org.team1.keyduck.common.exception;

public class InvalidBiddingPriceException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidBiddingPriceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

