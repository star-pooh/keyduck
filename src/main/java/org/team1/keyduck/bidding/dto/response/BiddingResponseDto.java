package org.team1.keyduck.bidding.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.bidding.entity.Bidding;

@Getter
public class BiddingResponseDto {

    private Long id;
    private Long auctionId;
    private String memberName;
    private Long biddingPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BiddingResponseDto(Long id, Long auctionId, String memberName, Long biddingPrice,
            LocalDateTime createdAt) {
        this.id = id;
        this.auctionId = auctionId;
        this.memberName = memberName;
        this.biddingPrice = biddingPrice;
        this.createdAt = createdAt;
    }

    public static BiddingResponseDto of(Bidding bidding) {
        return new BiddingResponseDto(
                bidding.getId(),
                bidding.getAuction().getId(),
                bidding.getMember().getName(),
                bidding.getPrice(),
                bidding.getCreatedAt()
        );
    }
}




