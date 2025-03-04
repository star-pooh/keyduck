package org.team1.keyduck.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.common.entity.BaseTime;
import org.team1.keyduck.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "payment")
public class Payment extends BaseTime {

    // 결제 식별자 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 결제 유저 정보
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 토스 주문 ID
    @Column(nullable = false, length = 30)
    private String orderId;

    // 결제 금액
    @Column(nullable = false)
    private Long amount;

    // 결제 수단
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    // 간편 결제 타입
    @Column(length = 30)
    private String easyPayType;

    // 결제 상태
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    // 결제가 일어난 날짜와 시간
    @Column(nullable = false)
    private LocalDateTime requestedAt;

    // 결제 승인이 일어난 날짜와 시간
    @Column(nullable = false)
    private LocalDateTime approvedAt;

    @Builder
    public Payment(Member member, String orderId, Long amount,
            PaymentMethod paymentMethod, String easyPayType, PaymentStatus paymentStatus,
            LocalDateTime requestedAt, LocalDateTime approvedAt) {
        this.member = member;
        this.orderId = orderId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.easyPayType = easyPayType;
        this.paymentStatus = paymentStatus;
        this.requestedAt = requestedAt;
        this.approvedAt = approvedAt;
    }
}
