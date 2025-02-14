package org.team1.keyduck.auction.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Getter
@AllArgsConstructor
public class AuctionCreateResponseDto {

    private Long keyboardId;

    private String title;

    private Long startPrice;

    private Long immediatePurchasePrice;

    private int biddingUnit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndTime;

    private AuctionStatus auctionStatus;

    public static AuctionCreateResponseDto of(Auction auction) {
        return new AuctionCreateResponseDto(
                auction.getKeyboard().getId(),
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                auction.getAuctionStatus()
        );
    }

}
