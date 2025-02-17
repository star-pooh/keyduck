package org.team1.keyduck.bidding.dto.response;

import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;

@Getter
public class SuccessBiddingResponseDto {

    private final Long auctionId;

    private final String auctionName;

    private final Long memberId;

    private final Long price;

    private SuccessBiddingResponseDto(Long auctionId, String auctionName, Long memberId,
            Long price) {
        this.auctionId = auctionId;
        this.auctionName = auctionName;
        this.memberId = memberId;
        this.price = price;
    }

    public static SuccessBiddingResponseDto of(Auction auction) {
        return new SuccessBiddingResponseDto(auction.getId(), auction.getTitle(),
                auction.getMember().getId(), auction.getCurrentPrice());
    }


}
