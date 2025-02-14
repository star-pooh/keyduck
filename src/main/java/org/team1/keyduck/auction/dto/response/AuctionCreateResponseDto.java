package org.team1.keyduck.auction.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.keyduck.auction.entity.Auction;

@Getter
public class AuctionCreateResponseDto {

    private final Long keyboardId;

    private final String title;

    private final Long startPrice;

    private final Long immediatePurchasePrice;

    private final Long currentPrice;

    private final int biddingUnit;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime biddingStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime biddingEndDate;

    private AuctionCreateResponseDto(Long keyboardId, String title, Long startPrice,
            Long immediatePurchasePrice, Long currentPrice, int biddingUnit,
            LocalDateTime biddingStartDate,
            LocalDateTime biddingEndDate) {
        this.keyboardId = keyboardId;
        this.title = title;
        this.startPrice = startPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.currentPrice = currentPrice;
        this.biddingUnit = biddingUnit;
        this.biddingStartDate = biddingStartDate;
        this.biddingEndDate = biddingEndDate;
    }

    public static AuctionCreateResponseDto of(Auction auction) {
        return new AuctionCreateResponseDto(
                auction.getKeyboard().getId(),
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getCurrentPrice(),
                auction.getBiddingUnit(),
                auction.getBiddingStartDate(),
                auction.getBiddingEndDate()
        );
    }

}
