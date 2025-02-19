package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;

@Getter
public class AuctionReadResponseDto {

    private Long auctionId;
    private Long keyboardId;
    private final String keyboardName;
    private final String keyboardDescription;
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
    private Long winnerId;
    private String winnerName;
    private List<BiddingResponseDto> biddings;


    private AuctionReadResponseDto(Long keyboardId, Long auctionId, String keyboardName,
            String keyboardDescription, String title, Long startPrice, Long currentPrice,
            Long immediatePurchasePrice, int biddingUnit, LocalDateTime auctionStartDate,
            LocalDateTime auctionEndDate, AuctionStatus auctionStatus, Long winnerId,
            String winnerName, List<BiddingResponseDto> biddings) {
        this.auctionId = auctionId;
        this.keyboardId = keyboardId;
        this.keyboardName = keyboardName;
        this.keyboardDescription = keyboardDescription;
        this.title = title;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
        this.auctionStatus = auctionStatus;
        this.winnerId = winnerId;
        this.winnerName = winnerName;
        this.biddings = biddings;
    }

    public static AuctionReadResponseDto of(Auction auction, List<BiddingResponseDto> biddings) {
        return new AuctionReadResponseDto(
                auction.getId(),
                auction.getKeyboard().getId(),
                auction.getKeyboard().getName(),
                auction.getKeyboard().getDescription(),
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getCurrentPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate(),
                auction.getAuctionStatus(),
                auction.getMember() == null ? null : auction.getMember().getId(),
                auction.getMember() == null ? null : auction.getMember().getName(),
                biddings
        );
    }
}