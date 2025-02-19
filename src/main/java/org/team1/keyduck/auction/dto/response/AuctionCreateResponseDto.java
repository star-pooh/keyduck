package org.team1.keyduck.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Getter;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.keyboard.dto.response.KeyboardDto;

@Getter
public class AuctionCreateResponseDto {

    private final Long auctionId;

    private final KeyboardDto keyboard;

    private final String title;

    private final Long startPrice;

    private final Long immediatePurchasePrice;

    private final Long currentPrice;

    private final int biddingUnit;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime auctionEndDate;

    private AuctionCreateResponseDto(Long auctionId, KeyboardDto keyboard, String title,
            Long startPrice, Long immediatePurchasePrice, Long currentPrice, int biddingUnit,
            LocalDateTime auctionStartDate, LocalDateTime auctionEndDate) {
        this.auctionId = auctionId;
        this.keyboard = keyboard;
        this.title = title;
        this.startPrice = startPrice;
        this.immediatePurchasePrice = immediatePurchasePrice;
        this.currentPrice = currentPrice;
        this.biddingUnit = biddingUnit;
        this.auctionStartDate = auctionStartDate;
        this.auctionEndDate = auctionEndDate;
    }

    public static AuctionCreateResponseDto of(Auction auction) {

        KeyboardDto keyboardDto = KeyboardDto.of(auction.getKeyboard());

        return new AuctionCreateResponseDto(
                auction.getId(),
                keyboardDto,
                auction.getTitle(),
                auction.getStartPrice(),
                auction.getImmediatePurchasePrice(),
                auction.getCurrentPrice(),
                auction.getBiddingUnit(),
                auction.getAuctionStartDate(),
                auction.getAuctionEndDate()
        );
    }

}
