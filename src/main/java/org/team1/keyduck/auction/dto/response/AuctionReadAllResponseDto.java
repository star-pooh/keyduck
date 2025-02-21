package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.keyboard.dto.response.AuctionKeyboardDto;

@Getter
public class AuctionReadAllResponseDto {

    private Long auctionId;
    private AuctionKeyboardDto keyboard;
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


    private AuctionReadAllResponseDto(Long auctionId, AuctionKeyboardDto keyboard, String title,
            Long startPrice, Long currentPrice, Long immediatePurchasePrice, int biddingUnit,
            LocalDateTime auctionStartDate, LocalDateTime auctionEndDate,
            AuctionStatus auctionStatus, Long winnerId, String winnerName) {
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

    }

    public AuctionReadAllResponseDto(Auction auction) {
        AuctionKeyboardDto keyboardDto = AuctionKeyboardDto.of(auction.getKeyboard());
        this.auctionId = auction.getId();
        this.keyboard = keyboardDto;
        this.title = auction.getTitle();
        this.startPrice = auction.getStartPrice();
        this.currentPrice = auction.getCurrentPrice();
        this.immediatePurchasePrice = auction.getImmediatePurchasePrice();
        this.biddingUnit = auction.getBiddingUnit();
        this.auctionStartDate = auction.getAuctionStartDate();
        this.auctionEndDate = auction.getAuctionEndDate();
        this.auctionStatus = auction.getAuctionStatus();
        this.winnerId = auction.getMember().getId();
        this.winnerName = auction.getMember().getName();
    }


    public static AuctionReadAllResponseDto of(Auction auction) {

        AuctionKeyboardDto keyboardDto = AuctionKeyboardDto.of(auction.getKeyboard());

        return new AuctionReadAllResponseDto(
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
                auction.getMember() == null ? null : auction.getMember().getName()
        );
    }

}
