package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.keyboard.dto.response.AuctionKeyboardDto;

@Getter
public class AuctionReadResponseDto {

    private Long auctionId;
    private AuctionKeyboardDto keyboard;
    private String title;
    private Long startPrice;
    private Long currentPrice;
    private Long immediatePurchasePrice;
    private Long biddingUnit;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionStartDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime auctionEndDate;
    private AuctionStatus auctionStatus;
    private Long winnerId;
    private String winnerName;
    private List<BiddingResponseDto> biddings;


    private AuctionReadResponseDto(Long auctionId, AuctionKeyboardDto keyboard, String title,
            Long startPrice, Long currentPrice, Long immediatePurchasePrice, Long biddingUnit,
            LocalDateTime auctionStartDate, LocalDateTime auctionEndDate,
            AuctionStatus auctionStatus, Long winnerId, String winnerName,
            List<BiddingResponseDto> biddings) {
        this.auctionId = auctionId;
        this.keyboard = keyboard;
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

        AuctionKeyboardDto keyboardDto = AuctionKeyboardDto.of(auction.getKeyboard());

        return new AuctionReadResponseDto(
                auction.getId(),
                keyboardDto,
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