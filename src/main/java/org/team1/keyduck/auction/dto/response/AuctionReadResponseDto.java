package org.team1.keyduck.auction.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Getter
@AllArgsConstructor
public class AuctionReadResponseDto {
    private String title;
    private Long startPrice;
    private Long currentPrice;
    private Long immediatePurchasePrice;
    private int biddingUnit;
    private LocalDateTime biddingStartDate;
    private LocalDateTime biddingEndDate;
    private AuctionStatus auctionStatus;

    public static AuctionReadResponseDto of(Auction auction) {
        return new AuctionReadResponseDto(
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getCurrentPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getBiddingStartDate(),
                auction.getBiddingEndDate(),
                auction.getAuctionStatus()
        );
    }
}
