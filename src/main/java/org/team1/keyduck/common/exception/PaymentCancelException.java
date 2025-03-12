package org.team1.keyduck.common.exception;

import lombok.Getter;
import org.team1.keyduck.payment.util.PaymentCancelErrorCode;

@Getter
public class PaymentCancelException extends RuntimeException {

    private final PaymentCancelErrorCode paymentCancelErrorCode;

    public PaymentCancelException(PaymentCancelErrorCode paymentCancelErrorCode) {
        super(paymentCancelErrorCode.getMessage());
        this.paymentCancelErrorCode = paymentCancelErrorCode;
    }
}
