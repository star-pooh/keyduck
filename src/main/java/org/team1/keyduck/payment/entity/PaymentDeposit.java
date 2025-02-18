package org.team1.keyduck.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.common.entity.BaseTime;
import org.team1.keyduck.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment_deposit")
public class PaymentDeposit extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private Long depositAmount;

    @Builder
    public PaymentDeposit(Member member, Long depositAmount) {
        this.member = member;
        this.depositAmount = depositAmount;
    }

    public void updatePaymentDeposit(Long amount) {
        this.depositAmount += amount;
    }

    public void deductedPrice(Long newBiddingPrice) {
        this.depositAmount -= newBiddingPrice;
    }
}
