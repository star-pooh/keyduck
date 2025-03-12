package org.team1.keyduck.common.exception;

import lombok.Getter;
import org.team1.keyduck.payment.util.PaymentConfirmErrorCode;

@Getter
public class PaymentConfirmException extends RuntimeException {

    private final PaymentConfirmErrorCode paymentConfirmErrorCode;

    public PaymentConfirmException(PaymentConfirmErrorCode paymentConfirmErrorCode) {
        super(paymentConfirmErrorCode.getMessage());
        this.paymentConfirmErrorCode = paymentConfirmErrorCode;
    }
}
