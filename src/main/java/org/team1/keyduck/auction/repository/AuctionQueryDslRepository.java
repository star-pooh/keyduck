package org.team1.keyduck.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.team1.keyduck.auction.dto.response.AuctionDto;

public interface AuctionQueryDslRepository {

    Page<AuctionDto.SearchResponse> findAllAuction(Pageable pageable, String keyboardName,
            String auctionTitle, String sellerName);
}
