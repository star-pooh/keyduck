package org.team1.keyduck.auction.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;

public interface AuctionQueryDslRepository {

    Slice<AuctionSearchResponseDto> findAllAuction(Long lastId, Pageable pageable,
            String keyboardName,
            String auctionTitle, String sellerName, String auctionStatus, String startDate,
            String endDate);

    List<Long> findOpenTargetAuction(LocalDateTime now);

    List<Long> findCloseTargetAuction(LocalDateTime now);
}
