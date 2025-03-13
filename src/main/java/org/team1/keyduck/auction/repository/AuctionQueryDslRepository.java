package org.team1.keyduck.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;

public interface AuctionQueryDslRepository {

    Page<AuctionSearchResponseDto> findAllAuction(Pageable pageable, String keyboardName,
            String auctionTitle, String sellerName, String auctionStatus, String startDate,
            String endDate);
}
