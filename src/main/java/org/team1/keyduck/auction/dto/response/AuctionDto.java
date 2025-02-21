package org.team1.keyduck.auction.dto.response;

import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.keyboard.dto.response.AuctionKeyboardDto;

public class AuctionDto {


    public record SearchRequest(String keyboardName, String auctionTitle, String sellerName) {

    }

    public record SearchResponse(Long auctionId, AuctionKeyboardDto keyboard, String title,
                                 Long currentPrice, Long immediatePurchasePrice,
                                 AuctionStatus auctionStatus, Long winnerId, String winnerName) {

        public SearchResponse(Auction auction) {
            this(
                    auction.getId(),
                    AuctionKeyboardDto.of(auction.getKeyboard()),
                    auction.getTitle(),
                    auction.getCurrentPrice(),
                    auction.getImmediatePurchasePrice(),
                    auction.getAuctionStatus(),
                    auction.getMember().getId(),
                    auction.getMember().getName()
            );
        }
    }
}
