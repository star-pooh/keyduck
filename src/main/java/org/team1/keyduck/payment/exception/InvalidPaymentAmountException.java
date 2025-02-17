package org.team1.keyduck.payment.exception;

import lombok.Getter;
import org.team1.keyduck.common.exception.ErrorCode;

@Getter
public class InvalidPaymentAmountException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidPaymentAmountException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
