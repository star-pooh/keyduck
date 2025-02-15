package org.team1.keyduck.bidding.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.bidding.entity.Bidding;

@Getter
public class BiddingResponseDto {

    private String auctionTitle;
    private String memberName;
    private Long biddingPrice;
    private LocalDateTime createdAt;

    public BiddingResponseDto(String auctionTitle, String memberName, Long biddingPrice,
            LocalDateTime createdAt) {
        this.auctionTitle = auctionTitle;
        this.memberName = memberName;
        this.biddingPrice = biddingPrice;
        this.createdAt = createdAt;
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


