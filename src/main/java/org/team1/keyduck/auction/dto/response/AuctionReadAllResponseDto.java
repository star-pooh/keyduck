package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;

@Getter
public class AuctionReadAllResponseDto {

    private String title;
    private Long startPrice;
    private Long currentPrice;
    private Long immediatePurchasePrice;
    private int biddingUnit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndDate;
    private AuctionStatus auctionStatus;


    private AuctionReadAllResponseDto(String title, Long startPrice, Long currentPrice,
            Long immediatePurchasePrice, int biddingUnit, LocalDateTime auctionStartDate,
            LocalDateTime auctionEndDate, AuctionStatus auctionStatus) {
        this.title = title;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.auctionStatus = auctionStatus;
    }

    public static AuctionReadAllResponseDto of(Auction auction) {
        return new AuctionReadAllResponseDto(
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getCurrentPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                auction.getAuctionStatus()
        );
    }

}
