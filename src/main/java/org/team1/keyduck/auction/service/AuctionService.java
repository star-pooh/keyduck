package org.team1.keyduck.auction.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final KeyboardRepository keyboardRepository;

    public AuctionCreateResponseDto createAuctionService(Long sellerId,
            AuctionCreateRequestDto requestDto) {
        //todo ErrorCode 추후에 키보드를 찾을 수 없다는 내용의 에러 코드 추가 후 익셉션 에러코드 변경이 필요함.
        Keyboard findKeyboard = keyboardRepository.findById(requestDto.getKeyboardId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!findKeyboard.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Auction auction = Auction.builder()
                .keyboard(findKeyboard)
                .title(requestDto.getTitle())
                .startPrice(requestDto.getStartPrice())
                .immediatePurchasePrice(requestDto.getImmediatePurchasePrice())
                .currentPrice(requestDto.getStartPrice())
                .biddingUnit(requestDto.getBiddingUnit())
                .biddingStartDate(requestDto.getBiddingStartDate())
                .biddingEndDate(requestDto.getBiddingEndDate())
                .auctionStatus(AuctionStatus.NOT_STARTED)
                .build();

        Auction saveAuction = auctionRepository.save(auction);

        return AuctionCreateResponseDto.of(saveAuction);

    }
}
