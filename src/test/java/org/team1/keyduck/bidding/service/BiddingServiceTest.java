package org.team1.keyduck.bidding.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.dto.response.SuccessBiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.member.entity.Address;
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
                OperationNotAllowedException.class, // 예외 타입
                () -> biddingService.createBidding(auctionId, price, authMember) // 실행
        );

        // 예외 메시지 검증
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

    @Test
    void getBiddingByAuction_성공() {
        //given
        Member member = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
                new Address("서울", "강남", "테헤란로", "상세주소", "상세주소"));
        memberRepository.save(member);

        Auction auction = Auction.builder()
                .member(member)
                .title("테스트 경매")
                .startPrice(10000L)
                .biddingUnit(10000)
                .build();
        auctionRepository.save(auction);

        Bidding bidding1 = new Bidding(auction, member, 30000L);
        Bidding bidding2 = new Bidding(auction, member, 40000L);

        when(auctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(biddingRepository.findByAuctionIdOrderByPriceDesc(auction.getId()))
                .thenReturn(List.of(bidding2, bidding1));

        //when
        List<BiddingResponseDto> result = biddingService.getBiddingByAuction(auction.getId());

        //then
        assertEquals(2, result.size());
        assertEquals(40000L, result.get(0).getBiddingPrice()); // 가장 높은 가격이 첫 번째
        assertEquals(30000L, result.get(1).getBiddingPrice()); // 두 번째 높은 가격
    }

    @Test
    void getsuccessbidding_성공() {
        Long memberId = 1L;
        int page = 1;

        Member member = new Member("gege", "gege@naver.com", "1234", MemberRole.CUSTOMER,
                new Address("서울", "강남", "테헤란로", "상세주소", "상세주소"));
        memberRepository.save(member);

        Auction auction1 = Auction.builder()
                .member(member)
                .title("낙찰경매1")
                .startPrice(10000L)
                .biddingUnit(10000)
                .auctionStatus(AuctionStatus.CLOSED)
                .build();
        auctionRepository.save(auction1);
        Bidding bidding1 = new Bidding(auction1, member, 50000L); //낙찰된 비딩

        Auction auction2 = Auction.builder()
                .member(member)
                .title("낙찰경매2")
                .startPrice(12000L)
                .biddingUnit(1000)
                .auctionStatus(AuctionStatus.CLOSED)
                .build();
        auctionRepository.save(auction2);
        Bidding bidding2 = new Bidding(auction2, member, 40000L); //낙찰된 비딩
        Bidding bidding3 = new Bidding(auction2, member, 20000L); //패찰된 비딩

        Auction auction3 = Auction.builder()
                .member(member)
                .title("패찰경매3")
                .startPrice(30000L)
                .biddingUnit(10000)
                .auctionStatus(AuctionStatus.CLOSED)
                .build();
        auctionRepository.save(auction3);
        Bidding bidding4 = new Bidding(auction3, member, 60000L); //패찰된 비딩
        Bidding bidding5 = new Bidding(auction3, member, 90000L); //낙찰된 비딩

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(auctionRepository.findAllByMember_IdAndAuctionStatus(member.getId(),
                AuctionStatus.CLOSED))
                .thenReturn(List.of(auction1, auction2, auction3));

        when(biddingRepository.findByMaxPriceAuctionId(auction1.getId())).thenReturn(member);
        when(biddingRepository.findByMaxPriceAuctionId(auction2.getId())).thenReturn(member);
        when(biddingRepository.findByMaxPriceAuctionId(auction3.getId())).thenReturn(member);

        List<SuccessBiddingResponseDto> expected = List.of(
                SuccessBiddingResponseDto.of(auction1),
                SuccessBiddingResponseDto.of(auction2),
                SuccessBiddingResponseDto.of(auction3)
        );

        //when
        Page<SuccessBiddingResponseDto> result = biddingService.getSuccessBidding(memberId, page);

        //then
        assertEquals(3, result.getTotalElements());

        List<SuccessBiddingResponseDto> actual = result.getContent();

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

}