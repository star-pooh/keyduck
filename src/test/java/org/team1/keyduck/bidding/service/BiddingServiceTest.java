package org.team1.keyduck.bidding.service;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;

@SpringBootTest
public class BiddingServiceTest {

    @Autowired
    private BiddingService biddingService;
    @Autowired
    private BiddingRepository biddingRepository;
    @Autowired
    private AuctionRepository auctionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private KeyboardRepository keyboardRepository;
    @Autowired
    private PaymentDepositRepository paymentDepositRepository;

    private Member member;
    private Auction auction;

    // public void 메소드명()
    // 판매자 멤버 생성
    // 키보드 생성
    // 경매 생성
    // for (100번) {
        // 구매자 멤버 생성
        // 예치금 데이터 생성
    // }

    @BeforeEach
    // 메소드명()
    public void setUp() {
        member = memberRepository.save(
                Member.builder()
                        .name("이름")
                        .email("email@example.com")
                        .password("password")
                        .memberRole(MemberRole.CUSTOMER)
                        .address(new Address("경기도", "안양시", "동안구", "12345", "1층"))
                        .build()
        );

        Keyboard keyboard = keyboardRepository.save(
                Keyboard.builder()
                        .member(member)
                        .name("키보드")
                        .description("키보드입니다.")
                        .build()
        );

        auction = auctionRepository.save(
                Auction.builder()
                        .title("키보드 경매")
                        .keyboard(keyboard)
                        .startPrice(1000L)
                        .currentPrice(1000L)
                        .biddingUnit(100)
                        .auctionStartDate(LocalDateTime.now())
                        .auctionEndDate(LocalDateTime.now().plusDays(1))
                        .auctionStatus(AuctionStatus.IN_PROGRESS)
                        .build()
        );

        paymentDepositRepository.save(PaymentDeposit.builder()
                .member(member)
                .depositAmount(1_000_000L)
                .build()
        );
    }

    @Test
    @DisplayName("락 없는 입찰 생성")
    public void createBiddingWithoutLock() throws InterruptedException {
        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            final long biddingPrice = auction.getCurrentPrice() + (i + 1) * 100L;
            executorService.execute(() -> {
                try {
                    biddingService.createBidding(
                            auction.getId(),
                            biddingPrice,
                            new AuthMember(member.getId(), member.getMemberRole())
                    );
                } catch (DataInvalidException e) { //
                    // 유저 아이디랑 입찰 금액 표시하기
                    System.out.println(e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        long biddingCount = biddingRepository.findAllByAuctionId(auction.getId()).size();
        System.out.println("총 입찰 횟수 : " + biddingCount);
    }

    @Test
    @DisplayName("비관적 락 이용한 입찰 생성")
    public void createBiddingWithPessimisticLock() throws InterruptedException {
        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            final long biddingPrice = auction.getCurrentPrice() + (i + 1) * 100L;
            executorService.execute(() -> {
                try {
                    biddingService.createBiddingWithPessimisticLock(
                            auction.getId(),
                            biddingPrice,
                            new AuthMember(member.getId(), member.getMemberRole())
                    );
                } catch (Exception e) {
                    System.out.println("입찰 실패 : " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        long biddingCount = biddingRepository.findAllByAuctionId(auction.getId()).size();
        System.out.println("총 입찰 횟수 : " + biddingCount);
    }
}