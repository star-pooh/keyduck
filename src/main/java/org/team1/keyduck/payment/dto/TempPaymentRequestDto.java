package org.team1.keyduck.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TempPaymentRequestDto {

    private final String orderId;

    private final Long paymentAmount;
}
