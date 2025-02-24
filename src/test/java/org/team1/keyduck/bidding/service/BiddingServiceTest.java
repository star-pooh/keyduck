package org.team1.keyduck.bidding.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.testdata.TestData;

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
    void createBidding_success_첫번쨰() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID1;
        Long memberId = TestData.TEST_ID2;
        Long price = 25000L;

        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION1; // ✅ 실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        when(biddingRepository.findByMember_IdAndAuction_Id(memberId, auctionId)).thenReturn(
                Optional.empty());

        Long myGreatBidding = 0L;

        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();
        ReflectionTestUtils.setField(bidding, "id", 2L);
        when(biddingRepository.save(any(Bidding.class))).thenReturn(bidding);

        doNothing().when(paymentDepositService).payBiddingPrice(anyLong(), anyLong(), anyLong());

        // when
        biddingService.createBidding(auctionId, price, authMember);

        // then
        assertEquals(price, auction.getCurrentPrice()); // ✅ 현재 가격이 업데이트되었는지 검증
        assertEquals(price, bidding.getPrice());
        assertNotNull(bidding);

        verify(paymentDepositService, times(1)).payBiddingPrice(memberId, price, myGreatBidding);
        verify(biddingRepository, times(1)).save(any(Bidding.class));
    }

    @Test
    void createBidding_success_두번째이상() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID1;
        Long memberId = TestData.TEST_ID2;
        Long price = 25000L; // ✅ 새로운 입찰가
        Long myGreatBidding = TestData.TEST_AUCTION1.getCurrentPrice(); // ✅ 기존 입찰가

        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION1; // ✅ 실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        when(biddingRepository.findByMember_IdAndAuction_Id(memberId, auctionId))
                .thenReturn(Optional.of(myGreatBidding)); // ✅ 기존 입찰 내역 존재

        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();
        ReflectionTestUtils.setField(bidding, "id", 2L);
        when(biddingRepository.save(any(Bidding.class))).thenReturn(bidding);

        doNothing().when(paymentDepositService).payBiddingPrice(anyLong(), anyLong(), anyLong());

        // when
        biddingService.createBidding(auctionId, price, authMember);

        // then
        assertEquals(price, auction.getCurrentPrice()); // ✅ 현재 가격이 업데이트되었는지 검증
        assertEquals(price, bidding.getPrice());
        assertNotNull(bidding);

        // ✅ 기존 입찰가가 `payBiddingPrice()`에서 정상적으로 반영되었는지 검증
        verify(paymentDepositService, times(1)).payBiddingPrice(memberId, price, myGreatBidding);

        // ✅ 새로운 입찰가로 저장되었는지 검증
        verify(biddingRepository, times(1)).save(any(Bidding.class));
    }


    @Test
    void createBidding_실패1_경매가_진행중이_아님() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID2; // ✅ 진행 중이 아닌 경매 사용
        Long memberId = TestData.TEST_ID2;
        Long price = 40000L;

        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION2; // ✅ 실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // when, then
        Exception exception = assertThrows(
                OperationNotAllowedException.class, // ✅ 예외 타입 검증
                () -> biddingService.createBidding(auctionId, price, authMember) // ✅ 실행
        );

        // ✅ 예외 메시지 검증
        assertEquals(ErrorCode.AUCTION_NOT_IN_PROGRESS,
                ((OperationNotAllowedException) exception).getErrorCode());
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
                OperationNotAllowedException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.MAX_BIDDING_COUNT_EXCEEDED,
                ((OperationNotAllowedException) exception).getErrorCode());
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
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.INVALID_BIDDING_PRICE_UNIT,
                ((DataInvalidException) exception).getErrorCode());
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
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.BIDDING_PRICE_BELOW_CURRENT_PRICE,
                ((DataInvalidException) exception).getErrorCode());
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
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );

        //메세지 검증
        assertEquals(ErrorCode.BIDDING_PRICE_EXCEEDS_MAX_LIMIT,
                ((DataInvalidException) exception).getErrorCode());
    }

}