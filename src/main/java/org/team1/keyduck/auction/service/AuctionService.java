package org.team1.keyduck.auction.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    // 경매 다건 조회
    @Transactional(readOnly = true)
    public List<AuctionReadAllResponseDto> findAllAuction() {

        // 전체 경매를 조회하고
        List<Auction> auctions = auctionRepository.findAll();

        // DTO로 변환 후 반환
        return auctions.stream()
                .map(AuctionReadAllResponseDto::of)
                .toList();
    }

}
