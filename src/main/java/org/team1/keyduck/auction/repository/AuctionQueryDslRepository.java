package org.team1.keyduck.auction.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;

public interface AuctionQueryDslRepository {

    Page<AuctionReadAllResponseDto> findAllAuction(Pageable pageable, String option);

}
