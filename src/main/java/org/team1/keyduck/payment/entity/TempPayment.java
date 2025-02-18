package org.team1.keyduck.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "temp_payment")
public class TempPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    Long memberId;

    @Column(nullable = false)
    String orderId;

    @Column(nullable = false)
    Long amount;

    @Builder
    public TempPayment(Long memberId, String orderId, Long amount) {
        this.memberId = memberId;
        this.orderId = orderId;
        this.amount = amount;
    }
}
