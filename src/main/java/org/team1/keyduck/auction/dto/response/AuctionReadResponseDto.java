package org.team1.keyduck.auction.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Getter
public class AuctionReadResponseDto {
    private String title;
    private Long startPrice;
    private Long currentPrice;
    private Long immediatePurchasePrice;
    private int biddingUnit;
    private LocalDateTime auctionStartDate;
    private LocalDateTime auctionEndDate;
    private AuctionStatus auctionStatus;

    private AuctionReadResponseDto(String title, Long startPrice, Long currentPrice,
            Long immediatePurchasePrice, int biddingUnit, LocalDateTime auctionStartDate,
            LocalDateTime auctionEndDate, AuctionStatus auctionStatus) {
        this.title = title;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.auctionStatus = auctionStatus;
    }

    public static AuctionReadResponseDto of(Auction auction) {
        return new AuctionReadResponseDto(
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getCurrentPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                auction.getAuctionStatus()
        );
    }
}
