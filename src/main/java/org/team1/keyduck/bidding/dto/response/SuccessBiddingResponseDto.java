package org.team1.keyduck.bidding.dto.response;

import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.bidding.entity.Bidding;

@Getter
public class SuccessBiddingResponseDto {

    private final Long id;

    private final Long auction_id;

    private final Long member_id;

    private final Long price;

    private final LocalDateTime createdAt;

    private SuccessBiddingResponseDto(Long id, Long auction_id, Long member_id, long price,
        LocalDateTime createdAt) {
        this.id = id;
        this.auction_id = auction_id;
        this.member_id = member_id;
        this.price = price;
        this.createdAt = createdAt;
    }

    public static SuccessBiddingResponseDto of(Bidding bidding) {
        return new SuccessBiddingResponseDto(bidding.getId(), bidding.getAuction().getId(),
            bidding.getMember().getId(), bidding.getPrice(), bidding.getCreatedAt());
    }


}
