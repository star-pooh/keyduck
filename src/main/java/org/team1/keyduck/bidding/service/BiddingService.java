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
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ErrorMessageParameter;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.payment.service.SaleProfitService;


@Service
@RequiredArgsConstructor
public class BiddingService {

    private final BiddingRepository biddingRepository;
    private final AuctionRepository auctionRepository;
    private final MemberRepository memberRepository;
    private final PaymentDepositService paymentDepositService;
    private final PaymentDepositRepository paymentDepositRepository;
    private final SaleProfitService saleProfitService;

    //비딩참여가 가능한 상태인지 검증
    private void validateBiddingAvailability(Auction auction, AuthMember authMember) {

        //경매가 진행 중이어야 가능
        if (!auction.getAuctionStatus().equals(AuctionStatus.IN_PROGRESS)) {
            throw new DataInvalidException(ErrorCode.AUCTION_NOT_IN_PROGRESS, null);
        }
        //비딩 횟수가 열번째 미만이어야함
        long biddingCount = biddingRepository.countByMember_IdAndAuction_Id(authMember.getId(),
                auction.getId());
        if (biddingCount >= 10) {
            throw new OperationNotAllowedException(ErrorCode.MAX_BIDDING_COUNT_EXCEEDED, null);
        }

    }

    private void validateBiddingPrice(Long price, Auction auction) {
        // 입찰가가 최소 입찰 단위 금액의 배수만큼 증가해야함
        long priceDifference = price - auction.getStartPrice();

        if (price.equals(auction.getImmediatePurchasePrice())) {
            return;
        }

        if (priceDifference % auction.getBiddingUnit() != 0) {
            throw new DataInvalidException(ErrorCode.INVALID_BIDDING_PRICE_UNIT, null);
        }

        //비딩 금액이 현재가보다 낮으면 안됨
        if (price <= auction.getCurrentPrice()) {
            throw new DataInvalidException(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE, null);
        }

        //비딩금액이 최대 입찰 호가 보다 높으면 안됨
        long maxPrice = auction.getCurrentPrice() + (auction.getBiddingUnit()
                * Constants.MAX_BIDDING_MULTIPLE);
        if (price > maxPrice) {
            throw new DataInvalidException(ErrorCode.BIDDING_PRICE_EXCEEDS_MAX_LIMIT, null);
        }

    }

    //즉구가 입찰시 로직
    private void instantBuy(Member member, Auction auction, Long price) {
        //만약 입찰가가 즉시구매가 라면
        if (price.equals(auction.getImmediatePurchasePrice())) {

            //낙찰자를 제외한 입찰자들의 최고 입찰가를 찾고
            List<Bidding> biddings = biddingRepository.findAllByIdBiddingMax(auction.getId());

            //경매의 낙찰자 정보에 즉구가를 입력한 유저의 아이디값을 저장하고
            auction.updateSuccessBiddingMember(member);

            //todo 추후 paymentDepositService로 책임분리 예정 / 책임분리 완료 후 로직 변경예정
            //낙찰자를 제외한 다른 입찰자들은 낙찰되지 않았으니, 포인트를 반환해주고
            for (Bidding bidding : biddings) {
                PaymentDeposit paymentDeposit = paymentDepositRepository.findByMember_Id(
                                bidding.getMember().getId())
                        .orElseThrow(
                                () -> new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER, "멤버"));
                paymentDeposit.updatePaymentDeposit(bidding.getPrice());
            }
            //낙찰자의 낙찰금액을 셀러에게 전달해주고
            saleProfitService.saleProfit(auction.getId());
            // 경매의 상태를 종료시킨다.
            auction.updateAuctionStatus(AuctionStatus.CLOSED);
        }
    }

    //생성 매서드
    @Transactional
    public void createBidding(Long auctionId, Long price, AuthMember authMember) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_AUCTION,
                        ErrorMessageParameter.AUCTION));

        Member member = memberRepository.findById(authMember.getId())
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER,
                        ErrorMessageParameter.MEMBER));

        validateBiddingAvailability(auction, authMember);
        validateBiddingPrice(price, auction);

        Long previousBiddingInfo = biddingRepository.findByMember_IdAndAuction_Id(member.getId(),
                auctionId).orElse(0L);

        paymentDepositService.payBiddingPrice(member.getId(), price, previousBiddingInfo);

        //입찰내역 생성
        Bidding newBiddings = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();

        //생성한 입찰내역을 저장
        biddingRepository.save(newBiddings);
        //경매의 현재가 업데이트
        auction.updateCurrentPrice(price);

        instantBuy(member, auction, price);
    }


    // 경매별 입찰 내역 조회
    @Transactional(readOnly = true)
    public List<BiddingResponseDto> getBiddingByAuction(Long auctionId) {
        List<Bidding> biddings = biddingRepository.findByAuctionIdOrderByPriceDesc(auctionId);

        return biddings.stream().map(BiddingResponseDto::of).toList();

    }

    @Transactional(readOnly = true)
    public Page<SuccessBiddingResponseDto> getSuccessBidding(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page - 1, Constants.SUCCESS_BIDDING_PAGE_SIZE);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_MEMBER,
                        ErrorMessageParameter.MEMBER));

        List<Auction> auctions = auctionRepository.findAllByMember_IdAndAuctionStatus(
                member.getId(),
                AuctionStatus.CLOSED);

        List<SuccessBiddingResponseDto> biddingResponseList = auctions.stream()
                .map(SuccessBiddingResponseDto::of).collect(Collectors.toList());

        return new PageImpl<>(biddingResponseList, pageable, biddingResponseList.size());
    }
}
