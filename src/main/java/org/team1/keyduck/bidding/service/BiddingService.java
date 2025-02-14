package org.team1.keyduck.bidding.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.BiddingNotAvailableException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.InvalidBiddingPriceException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;


@Service
@RequiredArgsConstructor
public class BiddingService {


    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;


    //경매 찾기
    private Auction findAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.AUCTION_NOT_FOUND));
    }

    // 검증
    private void validateAuction(Auction auction, Long price, AuthMember authMember) {
        validateBiddingAvailability(auction, authMember);
        validateBiddingPrice(price, auction);
    }

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
        //비딩 금액단위가 경매에 설정된 단위보다 작으면 안됨
        if (price % auction.getBiddingUnit() != 0) {
            throw new InvalidBiddingPriceException(ErrorCode.INVALID_BIDDING_PRICE_UNIT);
        }
        //비딩 금액이 현재가보다 낮으면 안됨
        if (price <= auction.getCurrentPrice()) {
            throw new InvalidBiddingPriceException(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE);
        }
        //비딩금액이 최대 입찰 호가 보다 높으면 안됨
        long maxPrice = auction.getCurrentPrice() + (auction.getBiddingUnit() * 10);
        if (price > maxPrice) {
            throw new InvalidBiddingPriceException(ErrorCode.BIDDING_PRICE_EXCEEDS_MAX_LIMIT);
        }
    }

    //생성 매서드
    @Transactional
    public void createBidding(Long auctionId, Long price,
            AuthMember authMember) {
        Auction auction = findAuctionById(auctionId);
        validateAuction(auction, price, authMember);

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();

        biddingRepository.save(bidding);
        auction.updateCurrentPrice(price);
    }


    // 경매별 입찰 내역 조회
    public List<BiddingResponseDto> getBiddingByAuction(Long auctionId) {
        List<Bidding> biddings = biddingRepository.findByAuctionIdOrderByPriceDesc(auctionId);

        return biddings.stream()
                .map(bidding -> new BiddingResponseDto(
                        bidding.getAuction().getTitle(),  // auctionTitle
                        bidding.getMember().getName(),    // memberName
                        bidding.getPrice(),               // biddingPrice
                        bidding.getCreatedAt()            // biddingTime
                ))
                .collect(Collectors.toList());

    }
}