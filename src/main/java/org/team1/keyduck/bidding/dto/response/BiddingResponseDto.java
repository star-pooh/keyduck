package org.team1.keyduck.bidding.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.bidding.entity.Bidding;

@Getter
public class BiddingResponseDto {

    Long id;
    private String auctionTitle;
    private String memberName;
    private Long biddingPrice;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private BiddingResponseDto(Long id, String auctionTitle, String memberName, Long biddingPrice,
            LocalDateTime createdAt) {
        this.id = id;
        this.auctionTitle = auctionTitle;
        this.memberName = memberName;
        this.biddingPrice = biddingPrice;
        this.createdAt = createdAt;
    }

    public static BiddingResponseDto of(Bidding bidding) {
        return new BiddingResponseDto(
                bidding.getId(),
                bidding.getAuction().getTitle(),
                bidding.getMember().getName(),
                bidding.getPrice(),
                bidding.getCreatedAt()
        );
    }
}