package org.team1.keyduck.bidding.dto.request;


import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.member.entity.Member;

@Getter
public class BiddingRequestDto {
    private Long memberId;
    private Long biddingId;
    private Long auctionId;

    @NotNull(message = "입찰가는 비울 수 없습니다.")
    private Long price;

    private LocalDateTime createdAt;



}
