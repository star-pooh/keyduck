package org.team1.keyduck.auction.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.keyduck.auction.entity.Auction;

@Getter
public class AuctionUpdateResponseDto {

    private final Long keyboardId;

    private final String title;

    private final Long startPrice;

    private final Long immediatePurchasePrice;

    private final int biddingUnit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionEndDate;

    private AuctionUpdateResponseDto(
            Long keyboardId, String title, Long startPrice,
            Long immediatePurchasePrice, int biddingUnit,
            LocalDateTime auctionStartDate, LocalDateTime auctionEndDate) {
        this.keyboardId = keyboardId;
        this.title = title;
        this.startPrice = startPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
    }

    public static AuctionUpdateResponseDto of(Auction auction) {
        return new AuctionUpdateResponseDto(
                auction.getKeyboard().getId(),
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate()
        );
    }
}
