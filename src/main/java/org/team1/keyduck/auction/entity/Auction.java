package org.team1.keyduck.auction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "auction")
public class Auction {

    // auction 식별자
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "keyboard_id")
    @OneToOne
    private Keyboard keyboard;

    @JoinColumn(name = "successful_bid_user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long startPrice;

    @Column
    private Long immediatePurchasePrice;

    @Column
    private Long currentPrice;

    @Column(nullable = false)
    private int biddingUnit;

    @Column(nullable = false)
    private LocalDateTime biddingStartDate;

    @Column(nullable = false)
    private LocalDateTime biddingEndDate;

    @Column
    @Enumerated(EnumType.STRING)
    private AuctionStatus auctionStatus;

    @Builder
    public Auction(Keyboard keyboard, Member member, String title, Long startPrice,
            Long immediatePurchasePrice,
            Long currentPrice, int biddingUnit, LocalDateTime biddingStartDate,
            LocalDateTime biddingEndDate, AuctionStatus auctionStatus) {
        this.keyboard = keyboard;
        this.member = member;
        this.title = title;
        this.startPrice = startPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.currentPrice = currentPrice;
        this.biddingUnit = biddingUnit;
        this.biddingStartDate = biddingStartDate;
        this.biddingEndDate = biddingEndDate;
        this.auctionStatus = auctionStatus;
    }
}

