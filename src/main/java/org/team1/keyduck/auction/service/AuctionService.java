package org.team1.keyduck.auction.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionReadAllResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.payment.service.SaleProfitService;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final KeyboardRepository keyboardRepository;
    private final BiddingRepository biddingRepository;

    private final SaleProfitService saleProfitService;
    private final PaymentDepositService paymentDepositService;

    public AuctionCreateResponseDto createAuctionService(Long sellerId,
            AuctionCreateRequestDto requestDto) {

        Keyboard findKeyboard = keyboardRepository
                .findByIdAndIsDeletedFalse(requestDto.getKeyboardId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_KEYBOARD,
                        ErrorMessageParameter.KEYBOARD));

        if (!findKeyboard.getMember().getId().equals(sellerId)) {
            throw new DataUnauthorizedAccessException(ErrorCode.FORBIDDEN_ACCESS, null);
        }

        Auction auction = Auction.builder()
                .keyboard(findKeyboard)
                .title(requestDto.getTitle())
                .startPrice(requestDto.getStartPrice())
                .immediatePurchasePrice(requestDto.getImmediatePurchasePrice())
                .currentPrice(requestDto.getStartPrice())
                .biddingUnit(requestDto.getBiddingUnit())
                .auctionStartDate(requestDto.getAuctionStartDate())
                .auctionEndDate(requestDto.getAuctionEndDate())
                .auctionStatus(AuctionStatus.NOT_STARTED)
                .build();

        Auction saveAuction = auctionRepository.save(auction);

        return AuctionCreateResponseDto.of(saveAuction);

    }

    @Transactional
    public AuctionUpdateResponseDto auctionModification(Long sellerId, Long auctionId,
            AuctionUpdateRequestDto requestDto) {

        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        if (!findAuction.getAuctionStatus().equals(AuctionStatus.NOT_STARTED)) {
            throw new DataInvalidException(ErrorCode.INVALID_STATUS,
                    ErrorMessageParameter.AUCTION_STATUS);
        }

        if (!findAuction.getKeyboard().getMember().getId().equals(sellerId)) {
            throw new DataUnauthorizedAccessException(ErrorCode.FORBIDDEN_ACCESS, null);
        }
        findAuction.updateAuction(requestDto);

        return AuctionUpdateResponseDto.of(findAuction);
    }

    // 경매 단건 조회
    @Transactional(readOnly = true)
    public AuctionReadResponseDto findAuction(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        // 경매 입찰 내역 조회
        List<BiddingResponseDto> responseDto = biddingRepository.findAllByAuctionId(auctionId)
                .stream()
                .map(BiddingResponseDto::of)
                .toList();

        return AuctionReadResponseDto.of(auction, responseDto);
    }

    // 경매 다건 조회
    @Transactional(readOnly = true)
    public Page<AuctionReadAllResponseDto> findAllAuction(Pageable pageable,
            String keyboardName, String auctionTitle, String sellerName) {

        // 전체 경매를 조회하고
        return auctionRepository.findAllAuction(pageable,
                keyboardName, auctionTitle, sellerName);

        // DTO로 변환 후 반환
//        return auctions.stream()
//                .map(AuctionReadAllResponseDto::of)
//                .toList();
    }

    @Transactional
    public void openAuction(Long memberId, Long auctionId) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        if (!findAuction.getAuctionStatus().equals(AuctionStatus.NOT_STARTED)) {
            throw new DataInvalidException(ErrorCode.INVALID_STATUS,
                    ErrorMessageParameter.AUCTION_STATUS);
        }

        if (!findAuction.getKeyboard().getMember().getId().equals(memberId)) {
            throw new DataUnauthorizedAccessException(ErrorCode.FORBIDDEN_ACCESS, null);
        }

        findAuction.updateAuctionStatus(AuctionStatus.IN_PROGRESS);
    }

    @Transactional
    public void closeAuction(Long id, Long auctionId) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        if (!findAuction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)) {
            throw new DataInvalidException(ErrorCode.INVALID_STATUS,
                    ErrorMessageParameter.AUCTION_STATUS);
        }

        if (!findAuction.getKeyboard().getMember().getId().equals(id)) {
            throw new DataUnauthorizedAccessException(ErrorCode.FORBIDDEN_ACCESS, null);
        }

        Member winnerMember = biddingRepository.findByMaxPriceAuctionId(auctionId);
        findAuction.updateSuccessBiddingMember(winnerMember);

        paymentDepositService.refundPaymentDeposit(auctionId);
        saleProfitService.saleProfit(auctionId);

        findAuction.updateAuctionStatus(AuctionStatus.CLOSED);
    }
}
