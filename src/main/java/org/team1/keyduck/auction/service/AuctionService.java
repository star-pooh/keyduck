package org.team1.keyduck.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;

    public AuctionUpdateResponseDto auctionModificationService(Long sellerId, Long auctionId,
            AuctionUpdateRequestDto requestDto) {

        //todo 추후에 경매정보를 찾을 수 없다는 내용의 에러 코드 추가 후 익셉션 에러코드 변경이 필요함.
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!findAuction.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        findAuction.updateAuction(requestDto);

        auctionRepository.save(findAuction);

        return AuctionUpdateResponseDto.of(findAuction);
    }
}
