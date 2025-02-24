package org.team1.keyduck.payment.entity;

import java.util.Arrays;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;

public enum PaymentMethod {

    CARD("카드"),
    TRANSFER("계좌이체"),
    EASY_PAY("간편결제"),
    VIRTUAL_ACCOUNT("가상계좌"),
    GIFT_CERTIFICATE("문화상품권"),
    MOBILE_PHONE("휴대폰");

    private final String paymentType;

    PaymentMethod(String paymentType) {
        this.paymentType = paymentType;
    }

    public static PaymentMethod getPaymentType(String paymentType) {
        return Arrays.stream(PaymentMethod.values())
                .filter(value -> value.paymentType.equals(paymentType))
                .findFirst()
                .orElseThrow(() -> new DataNotFoundException(
                        ErrorCode.NOT_FOUND_PAYMENT_METHOD, ErrorMessageParameter.PAYMENT_METHOD));
    }
}
