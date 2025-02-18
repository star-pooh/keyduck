package org.team1.keyduck.bidding.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.dto.response.SuccessBiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.BiddingNotAvailableException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.InvalidBiddingPriceException;
import org.team1.keyduck.common.util.GlobalConstants;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;


@Service
@RequiredArgsConstructor
public class BiddingService {


    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final PaymentDepositService paymentDepositService;

    //비딩참여가 가능한 상태인지 검증
    private void validateBiddingAvailability(Auction auction, AuthMember authMember) {

        //경매가 진행 중이어야 가능
        if (!auction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)) {
            throw new BiddingNotAvailableException(ErrorCode.AUCTION_NOT_IN_PROGRESS);
        }
        //비딩 횟수가 열번째 미만이어야함
        long biddingCount = biddingRepository.countByMember_IdAndAuction_Id(authMember.getId(),
                auction.getId());
        if (biddingCount >= 10) {
            throw new BiddingNotAvailableException(ErrorCode.MAX_BIDDING_COUNT_EXCEEDED);
        }

    }

    private void validateBiddingPrice(Long price, Auction auction) {
        // 입찰가가 최소 입찰 단위 금액의 배수만큼 증가해야함
        long priceDifference = price - auction.getStartPrice();
        if (priceDifference % auction.getBiddingUnit() != 0) {
            throw new InvalidBiddingPriceException(ErrorCode.INVALID_BIDDING_PRICE_UNIT);
        }

        //비딩 금액이 현재가보다 낮으면 안됨
        if (price <= auction.getCurrentPrice()) {
            throw new InvalidBiddingPriceException(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE);
        }

        //비딩금액이 최대 입찰 호가 보다 높으면 안됨
        long maxPrice = auction.getCurrentPrice() + (auction.getBiddingUnit() * 10L);
        if (price > maxPrice) {
            throw new InvalidBiddingPriceException(ErrorCode.BIDDING_PRICE_EXCEEDS_MAX_LIMIT);
        }
    }

    //생성 매서드
    @Transactional
    public void createBidding(Long auctionId, Long price, AuthMember authMember) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.AUCTION_NOT_FOUND));

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        validateBiddingAvailability(auction, authMember);
        validateBiddingPrice(price, auction);

        Long previousBiddingInfo = biddingRepository.findByMember_IdAndAuction_Id(member.getId(),
                auctionId);

        previousBiddingInfo = previousBiddingInfo == null ? 0 : previousBiddingInfo;

        paymentDepositService.payBiddingPrice(member.getId(), price, previousBiddingInfo);

        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();

        biddingRepository.save(bidding);

        //현재가 엽데이트
        auction.updateCurrentPrice(price);
    }


    // 경매별 입찰 내역 조회
    @Transactional(readOnly = true)
    public List<BiddingResponseDto> getBiddingByAuction(Long auctionId) {
        List<Bidding> biddings = biddingRepository.findByAuctionIdOrderByPriceDesc(auctionId);

        return biddings.stream().map(BiddingResponseDto::of).toList();

    }

    @Transactional(readOnly = true)
    public Page<SuccessBiddingResponseDto> getSuccessBidding(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page - 1, GlobalConstants.BIDDING_PAGE_SIZE);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        List<Auction> auctions = auctionRepository.findAllByMember_IdAndAuctionStatus(
                member.getId(),
                AuctionStatus.CLOSED);

        List<SuccessBiddingResponseDto> biddingResponseList = auctions.stream()
                .map(SuccessBiddingResponseDto::of).collect(Collectors.toList());

        return new PageImpl<>(biddingResponseList, pageable, biddingResponseList.size());
    }
}
