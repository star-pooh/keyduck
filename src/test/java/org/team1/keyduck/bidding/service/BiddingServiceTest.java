package org.team1.keyduck.bidding.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.BiddingNotAvailableException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.InvalidBiddingPriceException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BiddingServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private BiddingRepository biddingRepository;
    @Mock
    private PaymentDepositService paymentDepositService;

    @InjectMocks
    private BiddingService biddingService;

    @Test
    void createBidding_success() {
        //given
        Long auctionId = 1L;
        Long memberId = 1L;
        Long price = 40000L;
        Long myGreateBidding = 20000L;
        LocalDateTime dateTime = LocalDateTime.now();

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);
        when(auction.getBiddingUnit()).thenReturn(10000);
        when(auction.getCurrentPrice()).thenReturn(20000L);
        // 상태값 설정
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        //기존 최고 입찰가
        when(biddingRepository.findByMember_IdAndAuction_Id(memberId, auctionId)).thenReturn(
                myGreateBidding);

        //비딩 객체 생성
        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();
        ReflectionTestUtils.setField(bidding, "id", 1L);
        when(biddingRepository.save(any(Bidding.class))).thenReturn(bidding);

        //when
        biddingService.createBidding(auctionId, price, authMember);

        //then
        verify(biddingRepository, times(1)).save(any(Bidding.class));
        verify(paymentDepositService, times(1)).payBiddingPrice(memberId, price, myGreateBidding);
        verify(auction, times(1)).updateCurrentPrice(price);
    }

    @Test
    void createBidding_실패1_경매가_진행중이_아님() {
        //given
        Long auctionId = 1L;
        Long memberId = 1L;
        Long price = 40000L;
        LocalDateTime dateTime = LocalDateTime.now();

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Member member = mock(Member.class);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.NOT_STARTED);
        // 상태값 설정
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        //when,then
        Exception exception = assertThrows(
                BiddingNotAvailableException.class, // 예외 타입
                () -> biddingService.createBidding(auctionId, price, authMember) // 실행
        );

        // 예외 메시지 검증
        assertEquals(ErrorCode.AUCTION_NOT_IN_PROGRESS,
                ((BiddingNotAvailableException) exception).getErrorCode());

    }

    @Test
    void createBidding_실패2_비딩_횟수가_10번이상() {
        // given
        Long auctionId = 1L;
        Long price = 30000L;
        Long memberId = 1L;
        Long myGreatBidding = 20000L;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        //mock
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);
        when(auction.getBiddingUnit()).thenReturn(10000);
        when(auction.getCurrentPrice()).thenReturn(20000L);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // 비딩 횟수 초과
        when(biddingRepository.countByMember_IdAndAuction_Id(anyLong(), anyLong())).thenReturn(12L);

        //when/then
        Exception exception = assertThrows(
                BiddingNotAvailableException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.MAX_BIDDING_COUNT_EXCEEDED,
                ((BiddingNotAvailableException) exception).getErrorCode());
    }

    @Test
    void createBidding_실패3_비딩금액의_단위가_설정된_단위보다_작음() {
        // given
        Long auctionId = 1L;
        Long price = 35000L;
        Long memberId = 1L;
        Long myGreatBidding = 20000L;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);
        when(auction.getBiddingUnit()).thenReturn(10000);
        when(auction.getCurrentPrice()).thenReturn(20000L);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        //when, then
        Exception exception = assertThrows(
                InvalidBiddingPriceException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.INVALID_BIDDING_PRICE_UNIT,
                ((InvalidBiddingPriceException) exception).getErrorCode());
    }

    @Test
    void createBidding_실패4_현재가보다_낮은_입찰금액() {
        // given
        Long auctionId = 1L;
        Long price = 15000L;
        Long memberId = 1L;
        Long myGreatBidding = 10000L;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);
        when(auction.getBiddingUnit()).thenReturn(5000);
        when(auction.getCurrentPrice()).thenReturn(20000L);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        //when, then
        Exception exception = assertThrows(
                InvalidBiddingPriceException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE,
                ((InvalidBiddingPriceException) exception).getErrorCode());
    }

    @Test
    void createBidding_실패5_최대호가보다_높은_입찰금액() {
        // given
        Long auctionId = 1L;
        Long price = 100000000L;
        Long memberId = 1L;
        Long myGreatBidding = 20000L;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = mock(Auction.class);
        when(auction.getAuctionStatus()).thenReturn(AuctionStatus.IN_PROGRESS);
        when(auction.getBiddingUnit()).thenReturn(10000);
        when(auction.getCurrentPrice()).thenReturn(20000L);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        //when, then
        Exception exception = assertThrows(
                InvalidBiddingPriceException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.BIDDING_PRICE_EXCEEDS_MAX_LIMIT,
                ((InvalidBiddingPriceException) exception).getErrorCode());
    }
    
}