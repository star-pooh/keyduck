package org.team1.keyduck.bidding.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.member.entity.Member;

@Getter
public class BiddingResponseDto {

    private Auction auction;
    private Long biddingId;
    private Member member;
    private Long price;
    private LocalDateTime createdAt;

    public BiddingResponseDto(Bidding bidding) {
        this.auction = bidding.getAuction();
        this.biddingId = bidding.getId();
        this.member = bidding.getMember();
        this.price = bidding.getPrice();
    }


}
