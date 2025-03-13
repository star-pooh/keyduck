package org.team1.keyduck.auction.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionReadResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionSearchResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionQueryDslRepository;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.email.dto.EmailEvent;
import org.team1.keyduck.email.dto.MemberEmailRequestDto;
import org.team1.keyduck.email.service.EmailService;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.payment.service.SaleProfitService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final KeyboardRepository keyboardRepository;
    private final BiddingRepository biddingRepository;
    private final EmailService emailService;

    private final SaleProfitService saleProfitService;
    private final PaymentDepositService paymentDepositService;
    private final AuctionQueryDslRepository auctionQueryDslRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public AuctionCreateResponseDto createAuctionService(Long sellerId,
            AuctionCreateRequestDto requestDto) {
        if (auctionRepository.existsByKeyboard_Id(requestDto.getKeyboardId())) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_KEYBOARD, null);
        }

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

        MemberEmailRequestDto emailRequestDto = new MemberEmailRequestDto(
                Constants.AUCTION_CREATED_MAIL_TITLE,
                String.format(Constants.AUCTION_CREATED_MAIL_CONTENTS,
                        auction.getKeyboard().getMember().getName(),
                        auction.getKeyboard().getName(), auction.getTitle())
        );
        emailService.sendMemberEmail(auction.getKeyboard().getMember().getId(), emailRequestDto);
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
        List<BiddingResponseDto> responseDto = biddingRepository.findAllByAuctionIdOrderByCreatedAt(
                        auctionId)
                .stream()
                .map(BiddingResponseDto::of)
                .toList();

        return AuctionReadResponseDto.of(auction, responseDto);
    }

    // 경매 다건 조회
    @Transactional(readOnly = true)
    public Page<AuctionSearchResponseDto> findAllAuction(Pageable pageable,
            String keyboardName, String auctionTitle, String sellerName) {

        return auctionQueryDslRepository.findAllAuction(pageable,
                keyboardName, auctionTitle, sellerName);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void openAuction(Auction targetAuction) {
        targetAuction.updateAuctionStatus(AuctionStatus.IN_PROGRESS);

        log.info("auctionId : {}, auctionTitle : {}, in progress status change success",
                targetAuction.getId(), targetAuction.getTitle());

        MemberEmailRequestDto emailRequestDto = new MemberEmailRequestDto(
                Constants.AUCTION_OPEN_MAIL_TITLE,
                String.format(Constants.AUCTION_OPEN_MAIL_CONTENTS,
                        targetAuction.getKeyboard().getMember().getName(),
                        targetAuction.getKeyboard().getName(), targetAuction.getTitle())
        );
        applicationEventPublisher.publishEvent(
                new EmailEvent(targetAuction.getKeyboard().getMember().getId(), emailRequestDto));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void closeAuction(Auction targetAuction) {
        Member winnerMember = biddingRepository.findByMaxPriceAuctionId(targetAuction.getId());
        targetAuction.updateSuccessBiddingMember(winnerMember);

        paymentDepositService.refundPaymentDeposit(targetAuction.getId());
        saleProfitService.saleProfit(targetAuction.getId());

        targetAuction.updateAuctionStatus(AuctionStatus.CLOSED);

        log.info("auctionId : {}, auctionTitle : {}, closed status change success",
                targetAuction.getId(), targetAuction.getTitle());

        MemberEmailRequestDto emailRequestDto = new MemberEmailRequestDto(
                Constants.AUCTION_CLOSE_MAIL_TITLE,
                String.format(Constants.AUCTION_CLOSE_MAIL_CONTENTS,
                        targetAuction.getMember().getName(),
                        targetAuction.getKeyboard().getName(), targetAuction.getTitle())
        );
        applicationEventPublisher.publishEvent(
                new EmailEvent(targetAuction.getMember().getId(), emailRequestDto));
    }
}
