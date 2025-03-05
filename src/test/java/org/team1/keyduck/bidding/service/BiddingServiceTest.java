package org.team1.keyduck.bidding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.payment.service.SaleProfitService;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
public class BiddingServiceTest {

    @InjectMocks
    private BiddingService biddingService;
    @Mock
    private BiddingRepository biddingRepository;
    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private KeyboardRepository keyboardRepository;
    @Mock
    private PaymentDepositService paymentDepositService;
    @Mock
    private PaymentDepositRepository paymentDepositRepository;
    @Mock
    private SaleProfitService saleProfitService;


    //todo 성공케이스만들기
    @Test
    @DisplayName("성공: 첫번쨰 입찰")
    public void successCreateBiddingWithFirstBidding() {
        //given
        Long price = 22000L;
        Long auctionId = TestData.TEST_AUCTION_ID1;
        AuthMember authMember = new AuthMember(TestData.TEST_ID1, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION1;
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        when(biddingRepository.countByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(0L);

        when(biddingRepository.findByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(Optional.of(0L));

        doNothing().when(paymentDepositService)
                .payBiddingPrice(any(Long.class), any(Long.class), any(Long.class));

        when(biddingRepository.save(any(Bidding.class))).thenReturn(new Bidding());

        //when
        biddingService.createBidding(auctionId, price, authMember);
        //then
        verify(biddingRepository, times(1)).save(any(Bidding.class));
    }

    @Test
    @DisplayName("성공: 두번째 이상 입찰")
    public void successCreateBiddingWithSecondBidding() {
        //given
        Long price = 23000L;
        Long auctionId = TestData.TEST_AUCTION_ID2;
        AuthMember authMember = new AuthMember(TestData.TEST_ID1, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION2;
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        when(biddingRepository.countByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(1L);

        when(biddingRepository.findByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(Optional.of(1L));

        doNothing().when(paymentDepositService)
                .payBiddingPrice(any(Long.class), any(Long.class), any(Long.class));

        when(biddingRepository.save(any(Bidding.class))).thenReturn(new Bidding());

        //when
        biddingService.createBidding(auctionId, price, authMember);
        //then
        verify(biddingRepository, times(1)).save(any(Bidding.class));
    }

    @Test
    @DisplayName("성공: 즉시 구매가로 구매")
    public void successCreateBiddingWithImmediatePurchasePrice() {
        //given
        Long price = 100000L;
        Long auctionId = TestData.TEST_AUCTION_ID3;
        AuthMember authMember = new AuthMember(TestData.TEST_ID2, MemberRole.CUSTOMER);

        Auction auction = spy(TestData.TEST_AUCTION3);
        ReflectionTestUtils.setField(auction, "id", auctionId);
        ReflectionTestUtils.setField(auction, "immediatePurchasePrice", price);
        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        when(biddingRepository.countByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(0L);

        when(biddingRepository.findByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(Optional.of(0L));

        doNothing().when(paymentDepositService)
                .payBiddingPrice(any(Long.class), any(Long.class), any(Long.class));

        when(biddingRepository.save(any(Bidding.class))).thenReturn(new Bidding());

        doNothing().when(saleProfitService).saleProfit(any(Long.class));
        doNothing().when(paymentDepositService).refundPaymentDeposit(any(Long.class));
        doNothing().when(auction).updateSuccessBiddingMember(any(Member.class));
        doNothing().when(auction).updateAuctionStatus(any(AuctionStatus.class));

        //when
        biddingService.createBidding(auctionId, price, authMember);
        //then
        verify(biddingRepository, times(1)).save(any(Bidding.class));
        verify(saleProfitService, times(1)).saleProfit(any(Long.class));
        verify(paymentDepositService, times(1)).refundPaymentDeposit(any(Long.class));
        verify(auction, times(1)).updateSuccessBiddingMember(any(Member.class));
        verify(auction, times(1)).updateAuctionStatus(any(AuctionStatus.class));
    }

    @Test
    @DisplayName("실패: 진행중인 경매가 아닐때")
    public void failCreateBiddingWhenAuctionIsNotInProgress() {
        //given
        Long auctionId = TestData.TEST_AUCTION_ID4;
        Long memberId = TestData.TEST_ID2;
        Long price = 50000L;
        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION4;
        ReflectionTestUtils.setField(auction, "id", auctionId);

        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        OperationNotAllowedException exception1 = assertThrows(
                OperationNotAllowedException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("진행 중인 경매가 아닙니다.", exception1.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("실패: 비딩횟수가 10번을 초과했을 때")
    public void failCreateBiddingWhenBidsExceed() {
        //given
        Long auctionId = TestData.TEST_AUCTION_ID5;
        Long memberId = TestData.TEST_ID2;
        Long price = 50000L;
        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION5;
        ReflectionTestUtils.setField(auction, "id", auctionId);

        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        when(biddingRepository.countByMember_IdAndAuction_Id(any(Long.class),
                any(Long.class))).thenReturn(10L);

        OperationNotAllowedException exception1 = assertThrows(
                OperationNotAllowedException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰은 10번까지만 가능합니다.", exception1.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("실패: 입찰 단위에 맞지 않을 때")
    public void failCreateBiddingWhenNotFitBiddingUnit() {
        //given
        Long auctionId = TestData.TEST_AUCTION_ID6;
        Long memberId = TestData.TEST_ID2;
        Long price = 40100L;
        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION6;
        ReflectionTestUtils.setField(auction, "id", auctionId);

        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        DataInvalidException exception1 = assertThrows(
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("최소 입찰 금액 단위의 배수가 아닙니다.", exception1.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("실패: 현재가보다 낮은 입찰")
    public void failCreateBiddingWhenLowerThanCurrentPrice() {
        //given
        Long auctionId = TestData.TEST_AUCTION_ID6;
        Long memberId = TestData.TEST_ID2;
        Long price = 30000L;
        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION6;
        ReflectionTestUtils.setField(auction, "id", auctionId);

        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        DataInvalidException exception1 = assertThrows(
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰가가 현재가보다 작습니다.", exception1.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("실패: 최대 호가보다 높은 입찰")
    public void failCreateBiddingWhenHigherThanMax() {
        //given
        Long auctionId = TestData.TEST_AUCTION_ID6;
        Long memberId = TestData.TEST_ID2;
        Long price = 51000L;
        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        Auction auction = TestData.TEST_AUCTION6;
        ReflectionTestUtils.setField(auction, "id", auctionId);

        when(auctionRepository.findByIdWithPessimisticLock(any(Long.class))).thenReturn(
                Optional.of(auction));

        Member member = mock(Member.class);
        when(memberRepository.findById(any(Long.class))).thenReturn(Optional.of(member));

        DataInvalidException exception1 = assertThrows(
                DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰가가 1회 입찰 시 가능한 최대 금액(최소 입찰 금액 단위의 10배)을 초과하였습니다.",
                exception1.getErrorCode().getMessage());
    }

}

