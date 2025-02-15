package org.team1.keyduck.payment.entity;

import lombok.Getter;

@Getter
public enum PaymentStatus {

    ABORTED,
    CANCELED,
    DONE,
    EXPIRED,
    IN_PROGRESS,
    PARTIAL_CANCELED,
    WAITING_FOR_DEPOSIT
}
