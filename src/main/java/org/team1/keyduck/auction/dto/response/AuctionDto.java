package org.team1.keyduck.auction.dto.response;

import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.member.entity.Member;

public class AuctionDto {


    public record SearchRequest(String keyboardName, String auctionTitle, String sellerName) {

    }

    public record SearchResponse(Long auctionId, Long keyboardId, String keyboardName,
                                 String keyboardDescription, String title,
                                 Long currentPrice, Long immediatePurchasePrice,
                                 AuctionStatus auctionStatus, Long winnerId, String winnerName) {

        public SearchResponse(Long auctionId, Long keyboardId, String keyboardName,
                String keyboardDescription, String title, Long currentPrice,
                Long immediatePurchasePrice, AuctionStatus auctionStatus, Member member) {
            this(
                    auctionId,
                    keyboardId,
                    keyboardName,
                    keyboardDescription,
                    title,
                    currentPrice,
                    immediatePurchasePrice,
                    auctionStatus,
                    member.getId(),
                    member.getName()
            );
        }
    }
}
