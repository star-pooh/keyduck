package org.team1.keyduck.bidding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
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

    public void setUpBidding() {

        // 판매자 멤버 생성
        member = memberRepository.save(
                Member.builder()
                        .name("판매자")
                        .email("email@example.com")
                        .password("1234")
                        .memberRole(MemberRole.SELLER)
                        .address(new Address("경기도", "안양시", "동안구", "12345", "1층"))
                        .build()
        );

        // 키보드 생성
        Keyboard keyboard = keyboardRepository.save(
                Keyboard.builder()
                        .member(member)
                        .name("키보드")
                        .description("키보드입니다.")
                        .build()
        );

        // 경매 생성
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

        // for (100번) {
            // 구매자 멤버 생성
            // 예치금 데이터 생성
        // }
        // 100명의 구매자 생성 및 예치금 데이터 추가
        Faker faker = new Faker();

        for (int i = 0; i < 100; i++) {
            Member customer = memberRepository.save(
                    Member.builder()
                    .name("구매자" + i)
                    .email(faker.country().name() + faker.color() + i + "@" + faker.animal() + ".com")
                    .password("5678")
                    .memberRole(MemberRole.CUSTOMER)
                    .address(new Address("경기도", "안양시", "만안구", "56789", "2층"))
                    .build()
            );

            paymentDepositRepository.save(
                    PaymentDeposit.builder()
                            .member(customer)
                            .depositAmount(1_000_000L)
                            .build()
            );
        }
    }

    @BeforeEach
    // 메소드명()
    public void setUp() {
        setUpBidding();
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
                } catch (DataInvalidException e) {
                    // 유저 아이디랑 입찰 금액 표시하기
                    //System.out.println("유저 ID: " + member.getId() + ", 입찰 금액: " + biddingPrice);
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
    @DisplayName("락 없는 입찰 생성")
    public void createBiddingWithoutLock2() throws InterruptedException {
        List<Member> members = memberRepository.findAll(); // 전체 유저를 조회하고
        // customer만 담기
        List<Member> customers = new ArrayList<>();

        for (Member member : members) {
            if (member.getMemberRole() == MemberRole.CUSTOMER) {
                customers.add(member);
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            final long biddingPrice = auction.getCurrentPrice() + (i + 1) * 100L;
            final Member customer = customers.get(i);

            executorService.execute(() -> {
                try {
                    biddingService.createBidding(
                            auction.getId(),
                            biddingPrice,
                            new AuthMember(customer.getId(), customer.getMemberRole())
                    );
                } catch (DataInvalidException e) {
                    System.out.println("유저 ID: " + customer.getId() + ", 현재가: " + auction.getCurrentPrice()
                            + ", 입찰 금액: " + biddingPrice);
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

        // 최신 현재가
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();

        // 최종 입찰가
        Bidding lastBidding = biddingRepository.findByAuctionIdOrderByPriceDesc(auction.getId()).get(0);
        System.out.println("현재가 : " + updatedAuction.getCurrentPrice() + ", 입찰가 : " + lastBidding.getPrice());
        assertEquals(lastBidding.getPrice(), updatedAuction.getCurrentPrice());

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

    @Test
    @DisplayName("비관적 락 이용한 입찰 생성")
    public void createBiddingWithPessimisticLock2() throws InterruptedException {
        List<Member> members = memberRepository.findAll(); // 전체 유저를 조회하고
        // customer만 담기
        List<Member> customers = new ArrayList<>();

        for (Member member : members) {
            if (member.getMemberRole() == MemberRole.CUSTOMER) {
                customers.add(member);
            }
        }

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);

        for (int i = 0; i < 100; i++) {
            final long biddingPrice = auction.getCurrentPrice() + (i + 1) * 100L;
            final Member customer = customers.get(i);

            executorService.execute(() -> {
                try {
                    biddingService.createBiddingWithPessimisticLock(
                            auction.getId(),
                            biddingPrice,
                            new AuthMember(customer.getId(), customer.getMemberRole())
                    );
                } catch (DataInvalidException e) {
                    System.out.println("유저 ID: " + customer.getId() + ", 현재가: " + auction.getCurrentPrice()
                            + ", 입찰 금액: " + biddingPrice);
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

        // 최신 현재가
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();

        // 최종 입찰가
        Bidding lastBidding = biddingRepository.findByAuctionIdOrderByPriceDesc(auction.getId()).get(0);
        System.out.println("현재가 : " + updatedAuction.getCurrentPrice() + ", 입찰가 : " + lastBidding.getPrice());
        assertEquals(lastBidding.getPrice(), updatedAuction.getCurrentPrice());

    }
}

