package org.team1.keyduck.common.exception;



public class AuctionNotInProgressException extends RuntimeException {

    private final ErrorCode errorCode;

    public AuctionNotInProgressException(ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}

