package org.team1.keyduck.payment;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.payment.dto.PaymentDto;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
public class PaymentDepositServiceTest {

    @Mock
    PaymentDepositRepository paymentDepositRepository;

    @Mock
    BiddingRepository biddingRepository;

    @InjectMocks
    PaymentDepositService paymentDepositService;

    @Test
    @DisplayName("성공_새로운 경매 포인트 저장")
    public void successCreateNewPaymentDeposit() {
        // given
        PaymentDto paymentDto = mock(PaymentDto.class);
        Member member = mock(Member.class);

        when(paymentDto.getMember()).thenReturn(member);
        when(paymentDto.getMember().getId()).thenReturn(TestData.TEST_ID1);
        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.empty());

        // when
        paymentDepositService.processPaymentDeposit(paymentDto);

        // then
        verify(paymentDepositRepository, times(1)).save(any(PaymentDeposit.class));
    }

    @Test
    @DisplayName("성공_기존 경매 포인트에 누적")
    public void successExistPaymentDeposit() {
        // given
        PaymentDeposit existPaymentDeposit = mock(PaymentDeposit.class);
        PaymentDto paymentDto = mock(PaymentDto.class);
        Member member = mock(Member.class);

        when(paymentDto.getMember()).thenReturn(member);
        when(paymentDto.getMember().getId()).thenReturn(TestData.TEST_ID1);
        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.of(existPaymentDeposit));

        // when
        paymentDepositService.processPaymentDeposit(paymentDto);

        // then
        verify(existPaymentDeposit, times(1)).updatePaymentDeposit(any(Long.class));
        verify(paymentDepositRepository, never()).save(any(PaymentDeposit.class));
    }

    @Test
    @DisplayName("성공_첫 번째 입찰에 대한 경매 포인트 지불")
    public void successPayBiddingPriceWithFirstTime() {
        // given
        Long newBiddingPrice = 2000L;
        Long lastBiddingPrice = 0L;
        PaymentDeposit paymentDeposit = TestData.TEST_PAYMENT_DEPOSIT1;

        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.of(paymentDeposit));

        // when
        paymentDepositService.payBiddingPrice(any(Long.class), newBiddingPrice, lastBiddingPrice);

        // then
        verify(paymentDepositRepository, times(1)).findByMember_Id(any(Long.class));
        assertThat(3000L).isEqualTo(paymentDeposit.getDepositAmount());
    }

    @Test
    @DisplayName("성공_두 번째 입찰에 대한 경매 포인트 지불")
    public void successPayBiddingPriceWithSecondTime() {
        // given
        Long newBiddingPrice = 2000L;
        Long lastBiddingPrice = 1000L;
        PaymentDeposit paymentDeposit = TestData.TEST_PAYMENT_DEPOSIT2;

        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.of(paymentDeposit));

        // when
        paymentDepositService.payBiddingPrice(any(Long.class), newBiddingPrice, lastBiddingPrice);

        // then
        verify(paymentDepositRepository, times(1)).findByMember_Id(any(Long.class));
        assertThat(4000L).isEqualTo(paymentDeposit.getDepositAmount());
    }

    @Test
    @DisplayName("실패_경매 포인트에 대한 멤버 정보를 찾을 수 없음")
    public void failPayBiddingPriceWithNotFoundMember() {
        // given
        Long newBiddingPrice = 2000L;
        Long lastBiddingPrice = 1000L;
        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.empty());

        // when
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> paymentDepositService.payBiddingPrice(TestData.TEST_ID1,
                        newBiddingPrice, lastBiddingPrice));

        // then
        assertThat("해당 멤버을(를) 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("실패_잔액 부족으로 인한 입찰가 지불 불가")
    public void failPayBiddingPriceWithInsufficientPaymentDeposit() {
        // given
        Long newBiddingPrice = 20000L;
        Long lastBiddingPrice = 10000L;
        PaymentDeposit paymentDeposit = TestData.TEST_PAYMENT_DEPOSIT1;

        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.of(paymentDeposit));

        // when
        DataInvalidException exception = assertThrows(DataInvalidException.class,
                () -> paymentDepositService.payBiddingPrice(TestData.TEST_ID1,
                        newBiddingPrice, lastBiddingPrice));

        // then
        assertThat("입찰 가능한 예치금이 부족합니다.").isEqualTo(exception.getMessage());
    }

    @Test
    @DisplayName("성공_패찰자에 대한 경매 포인트 환급 완료")
    public void successRefundPaymentDeposit() {
        // given
        Long auctionId = 1L;
        Auction auction = mock(Auction.class);
        Member member1 = TestData.TEST_MEMBER1;
        ReflectionTestUtils.setField(member1, "id", 1L);

        Member member2 = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member2, "id", 2L);

        Bidding bidding1 = Bidding.builder()
                .auction(auction)
                .member(member1)
                .price(1000L)
                .build();

        Bidding bidding2 = Bidding.builder()
                .auction(auction)
                .member(member2)
                .price(2500L)
                .build();

        PaymentDeposit paymentDeposit1 = PaymentDeposit.builder()
                .member(member1)
                .depositAmount(TestData.TEST_DEPOSIT_AMOUNT)
                .build();

        PaymentDeposit paymentDeposit2 = PaymentDeposit.builder()
                .member(member2)
                .depositAmount(TestData.TEST_DEPOSIT_AMOUNT)
                .build();

        List<Bidding> maxBiddingPricePerMember = List.of(bidding1, bidding2);

        when(biddingRepository.findAllByIdBiddingMax(auctionId)).thenReturn(
                maxBiddingPricePerMember);
        when(paymentDepositRepository.findByMember_Id(member1.getId())).thenReturn(
                Optional.of(paymentDeposit1));
        when(paymentDepositRepository.findByMember_Id(member2.getId())).thenReturn(
                Optional.of(paymentDeposit2));

        // when
        paymentDepositService.refundPaymentDeposit(auctionId);

        // then
        verify(paymentDepositRepository, times(1)).findByMember_Id(member1.getId());
        verify(paymentDepositRepository, times(1)).findByMember_Id(member2.getId());

        assertThat(6000L).isEqualTo(paymentDeposit1.getDepositAmount());
        assertThat(7500L).isEqualTo(paymentDeposit2.getDepositAmount());
    }

    @Test
    @DisplayName("실패_경매 포인트를 환급해줄 멤버를 찾을 수 없음")
    public void failRefundPaymentDepositWithNotFoundMember() {
        // given
        Long auctionId = 1L;
        Auction auction = mock(Auction.class);
        Member member1 = mock(Member.class);
        Member member2 = mock(Member.class);

        Bidding bidding1 = Bidding.builder()
                .auction(auction)
                .member(member1)
                .price(1000L)
                .build();

        Bidding bidding2 = Bidding.builder()
                .auction(auction)
                .member(member2)
                .price(2500L)
                .build();

        List<Bidding> maxBiddingPricePerMember = List.of(bidding1, bidding2);

        when(biddingRepository.findAllByIdBiddingMax(auctionId)).thenReturn(
                maxBiddingPricePerMember);
        when(paymentDepositRepository.findByMember_Id(any(Long.class))).thenReturn(
                Optional.empty());

        // when
        DataNotFoundException exception = assertThrows(DataNotFoundException.class,
                () -> paymentDepositService.refundPaymentDeposit(auctionId));

        // then
        assertThat("해당 멤버을(를) 찾을 수 없습니다.").isEqualTo(exception.getMessage());
    }
}
