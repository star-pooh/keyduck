package org.team1.keyduck.auction.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Getter
public class AuctionSearchResponseDto {

    private final Long auctionId;

    private final Long keyboardId;

    private final String keyboardName;

    private final String keyboardDescription;

    private final String title;

    private final Long currentPrice;

    private final Long immediatePurchasePrice;

    private final AuctionStatus auctionStatus;

    private final Long winnerId;

    private final String winnerName;

    @QueryProjection
    public AuctionSearchResponseDto(Long auctionId, Long keyboardId, String keyboardName,
            String keyboardDescription, String title, Long currentPrice,
            Long immediatePurchasePrice, AuctionStatus auctionStatus, Long winnerId,
            String winnerName) {
        this.auctionId = auctionId;
        this.keyboardId = keyboardId;
        this.keyboardName = keyboardName;
        this.keyboardDescription = keyboardDescription;
        this.title = title;
        this.currentPrice = currentPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.auctionStatus = auctionStatus;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
    }


}
