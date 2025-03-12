package org.team1.keyduck.auction.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.entity.Auction;

public interface AuctionQueryDslRepository {

    Page<AuctionSearchResponseDto> findAllAuction(Pageable pageable, String keyboardName,
            String auctionTitle, String sellerName);

    List<Auction> findOpenTargetAuction(LocalDateTime now);

    List<Auction> findCloseTargetAuction(LocalDateTime now);
}
