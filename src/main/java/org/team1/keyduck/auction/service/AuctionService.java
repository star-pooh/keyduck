package org.team1.keyduck.auction.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final KeyboardRepository keyboardRepository;
    private final BiddingRepository biddingRepository;
    private final PaymentDepositRepository paymentDepositRepository;
    private final MemberRepository memberRepository;

    public AuctionCreateResponseDto createAuctionService(Long sellerId,
            AuctionCreateRequestDto requestDto) {
        //todo ErrorCode 추후에 키보드를 찾을 수 없다는 내용의 에러 코드 추가 후 익셉션 에러코드 변경이 필요함.
        Keyboard findKeyboard = keyboardRepository.findByIdAndIsDeletedFalse(
                        requestDto.getKeyboardId())
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

        //todo 추후에 경매정보를 찾을 수 없다는 내용의 에러 코드 추가 후 익셉션 에러코드 변경이 필요함.
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        //todo 추후 시작되거나 종료된 경매에 대해서는 수정할 수 없다는 내용의 익셉션을 생성하여 적용필요.
        if (!findAuction.getAuctionStatus().equals(AuctionStatus.NOT_STARTED)) {
            throw new RuntimeException("진행중 이거나 종료된 경매는 수정할 수 없습니다.");
        }

        if (!findAuction.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }
        findAuction.updateAuction(requestDto);

        return AuctionUpdateResponseDto.of(findAuction);
    }

    // 경매 단건 조회
    @Transactional(readOnly = true)
    public AuctionReadResponseDto findAuction(Long auctionId) {

        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        // 경매 입찰 내역 조회
        List<BiddingResponseDto> responseDto = biddingRepository.findAllByAuctionId(auctionId)
                .stream()
                .map(BiddingResponseDto::of)
                .toList();

        return AuctionReadResponseDto.of(auction, responseDto);
    }

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

    @Transactional
    public void openAuction(Long id, Long auctionId) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.AUCTION_NOT_FOUND));

        if (!findAuction.getAuctionStatus().equals(AuctionStatus.NOT_STARTED)) {
            throw new DataNotFoundException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if (!findAuction.getKeyboard().getMember().getId().equals(id)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        findAuction.updateAuctionStatus(AuctionStatus.IN_PROGRESS);
    }

    @Transactional
    public void closeAuction(Long id, Long auctionId) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.AUCTION_NOT_FOUND));

        if (!findAuction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)) {
            throw new DataNotFoundException(ErrorCode.FORBIDDEN_ACCESS);
        }

        if (!findAuction.getKeyboard().getMember().getId().equals(id)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Member winnerMember = biddingRepository.findByMaxPriceAuctionId(auctionId);

        findAuction.updateSuccessBiddingMember(winnerMember);

        List<Bidding> biddings = biddingRepository.findAllByIdBiddingMax(auctionId);

        for (Bidding bidding : biddings) {
            PaymentDeposit paymentDeposit = paymentDepositRepository.findByMember_Id(
                            bidding.getMember().getId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            paymentDeposit.updatePaymentDeposit(bidding.getPrice());
        }

        findAuction.updateAuctionStatus(AuctionStatus.CLOSED);
    }
}
