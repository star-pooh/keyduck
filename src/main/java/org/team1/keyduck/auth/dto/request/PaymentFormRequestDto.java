package org.team1.keyduck.auth.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentFormRequestDto {

    private final String email;

    private final String password;

    private final Long amount;
}
