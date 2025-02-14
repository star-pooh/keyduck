package org.team1.keyduck.bidding.dto.response;

import java.time.LocalDateTime;
import org.team1.keyduck.bidding.entity.Bidding;

public class BiddingResponseDto {

    private String auctionTitle;
    private String memberName;
    private Long biddingPrice;
    private LocalDateTime biddingTime;

    public BiddingResponseDto(String auctionTitle, String memberName, Long biddingPrice,
            LocalDateTime biddingTime) {
        this.auctionTitle = auctionTitle;
        this.memberName = memberName;
        this.biddingPrice = biddingPrice;
        this.biddingTime = biddingTime;
    }

    public static BiddingResponseDto of(Bidding bidding) {
        return new BiddingResponseDto(
                bidding.getAuction().getTitle(),
                bidding.getMember().getName(),
                bidding.getPrice(),
                bidding.getCreatedAt()
        );
    }
}


