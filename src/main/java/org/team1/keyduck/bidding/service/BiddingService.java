package org.team1.keyduck.bidding.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.dto.request.BiddingRequestDto;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.AuctionNotInProgressException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.InvalidBiddingPriceException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class BiddingService {


    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;


    //경매 찾기
    private Auction findAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId).orElseThrow(()->new DataNotFoundException(ErrorCode.AUCTION_NOT_FOUND));
    }

    // 검증
    private void validateAuction(Auction auction, Long price) {
        //경매가 진행 중이어야 가능
        if (!auction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)){
            throw new AuctionNotInProgressException(ErrorCode.AUCTION_NOT_IN_PROGRESS);
        }
        //비딩 금액단위가 경매에 설정된 단위보다 작으면 안됨
        if (price % auction.getBiddingUnit() !=0){
            throw new InvalidBiddingPriceException(ErrorCode.INVALID_BIDDING_PRICE_UNIT);
        }
        //비딩 금액이 현재가보다 낮으면 안됨
        if(price <=auction.getCurrentPrice()){
            throw new InvalidBiddingPriceException(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE);
        }

    }

    //생성 매서드
    public Bidding createBidding(Long auctionId, Long price, Member member) {
        Auction auction = findAuctionById(auctionId);
        validateAuction(auction, price);


        Bidding bidding = Bidding.builder()
                        .auction(auction)
                        .member(member)
                        .price(price)
                        .build();

        return biddingRepository.save(bidding) ;
    }

}
