package org.team1.keyduck.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    // @docs https://docs.tosspayments.com/reference#객체-상세-2
    ABORTED,
    CANCELED,
    DONE,
    EXPIRED,
    IN_PROGRESS,
    PARTIAL_CANCELED,
    WAITING_FOR_DEPOSIT
}
