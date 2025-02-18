package org.team1.keyduck.payment.dto;

import lombok.Getter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.entity.Payment;

@Getter
public class PaymentDto {

    private final Member member;

    private final Long amount;

    private PaymentDto(Member member, Long amount) {
        this.member = member;
        this.amount = amount;
    }

    public static PaymentDto of(Payment payment) {
        return new PaymentDto(
                payment.getMember(),
                payment.getAmount()
        );
    }
}
