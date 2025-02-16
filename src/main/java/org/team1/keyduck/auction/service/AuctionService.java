package org.team1.keyduck.auction.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.dto.request.AuctionCreateRequestDto;
import org.team1.keyduck.auction.dto.request.AuctionUpdateRequestDto;
import org.team1.keyduck.auction.dto.response.AuctionCreateResponseDto;
import org.team1.keyduck.auction.dto.response.AuctionUpdateResponseDto;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
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
        Keyboard findKeyboard = keyboardRepository.findById(requestDto.getKeyboardId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        if (!findKeyboard.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        Auction auction = Auction.builder().keyboard(findKeyboard).member(findKeyboard.getMember())
                .title(requestDto.getTitle()).startPrice(requestDto.getStartPrice())
                .immediatePurchasePrice(requestDto.getImmediatePurchasePrice())
                .currentPrice(requestDto.getStartPrice()).biddingUnit(requestDto.getBiddingUnit())
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

        if (!findAuction.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }
        findAuction.updateAuction(requestDto);

        return AuctionUpdateResponseDto.of(findAuction);
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

        Bidding greatBidding = biddingRepository.findByMaxPriceAuctionId(auctionId);

        Member member = memberRepository.findById(greatBidding.getMember().getId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        findAuction.updateSuccessBiddingMember(member);

        List<Bidding> biddings = biddingRepository.findAllByIdBiddingMax(auctionId);

        for (Bidding bidding : biddings) {
            PaymentDeposit paymentDeposit = paymentDepositRepository.findByMember_Id(
                            bidding.getMember().getId())
                    .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));
            paymentDeposit.refundPrice(bidding.getPrice());
        }

        findAuction.updateAuctionStatus(AuctionStatus.CLOSED);
    }
}
