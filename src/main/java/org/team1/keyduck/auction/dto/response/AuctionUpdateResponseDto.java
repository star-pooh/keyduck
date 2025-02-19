package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;

@Getter
public class AuctionUpdateResponseDto {

    private final Long auctionId;

    private final Long keyboardId;

    private final String keyboardName;

    private final String keyboardDescription;

    private final String title;

    private final Long startPrice;

    private final Long immediatePurchasePrice;

    private final int biddingUnit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionEndDate;

    private AuctionUpdateResponseDto(
            Long auctionId, Long keyboardId, String keyboardName, String keyboardDescription,
            String title, Long startPrice, Long immediatePurchasePrice, int biddingUnit,
            LocalDateTime auctionStartDate, LocalDateTime auctionEndDate) {
        this.auctionId = auctionId;
        this.keyboardId = keyboardId;
        this.keyboardName = keyboardName;
        this.keyboardDescription = keyboardDescription;
        this.title = title;
        this.startPrice = startPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
    }

    public static AuctionUpdateResponseDto of(Auction auction) {
        return new AuctionUpdateResponseDto(
                auction.getId(),
                auction.getKeyboard().getId(),
                auction.getKeyboard().getName(),
                auction.getKeyboard().getDescription(),
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate()
        );
    }
}
