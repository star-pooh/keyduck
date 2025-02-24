package org.team1.keyduck.auth.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentFormResponseDto {

    private final String token;

    private final Long amount;

}
