package org.team1.keyduck.bidding.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.team1.keyduck.auction.entity.Auction;
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
import org.team1.keyduck.testdata.TestData;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private PaymentDepositService paymentDepositService;

    private static final Logger log = LoggerFactory.getLogger(BiddingServiceTest.class);
    @Autowired
    private PaymentDepositRepository paymentDepositRepository;

    @BeforeEach
    public void setup() {
        setUpTestData();
    }

    private void setUpTestData() {
        memberRepository.save(TestData.TEST_MEMBER1);
        memberRepository.save(TestData.TEST_MEMBER2);
        memberRepository.save(TestData.TEST_MEMBER3);
        keyboardRepository.save(TestData.TEST_KEYBOARD1);
        keyboardRepository.save(TestData.TEST_KEYBOARD2);
        keyboardRepository.save(TestData.TEST_KEYBOARD3);
        keyboardRepository.save(TestData.TEST_KEYBOARD4);
        auctionRepository.save(TestData.TEST_AUCTION1);
        auctionRepository.save(TestData.TEST_AUCTION2);
        auctionRepository.save(TestData.TEST_AUCTION3);
        auctionRepository.save(TestData.TEST_AUCTION4);
        biddingRepository.save(TestData.TEST_BIDDING1);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT1);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT2);
    }

    @Test
    @Commit
    @DisplayName("비관적 락 이용한 입찰 생성")
    public void createBidding_success() throws InterruptedException {
        Long auctionId = TestData.TEST_AUCTION4.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 50000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        biddingService.createBidding(auctionId, price, authMember);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        // 전체 유저 목록에서 CUSTOMER 역할의 멤버들만 필터링
        List<Member> members = memberRepository.findAll();
        List<Member> customers = members.stream()
                .filter(m -> m.getMemberRole() == MemberRole.CUSTOMER)
                .collect(Collectors.toList());

        for (int i = 0; i < 10; i++) {
            final long biddingPrice = price + (i + 1) * 1000L;
            executorService.execute(() -> {
                try {
                    biddingService.createBidding(
                            auctionId,
                            biddingPrice,
                            new AuthMember(TestData.TEST_ID2, TestData.TEST_MEMBER_ROLE2));
                } catch (DataInvalidException e) {
                }
            });
        }
        executorService.shutdown();

        Auction updatedAuction = auctionRepository.findById(auctionId).get();
        Bidding lastBidding = biddingRepository.findByAuctionIdOrderByPriceDesc(auctionId).get(0);
        assertEquals(lastBidding.getPrice(), updatedAuction.getCurrentPrice());
    }

    @Test
    @DisplayName("진행중인 경매가 아닐때")
    public void createBidding_fail_Auction_Not_Inprogress() {
        Long auctionId = TestData.TEST_AUCTION2.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 25000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        OperationNotAllowedException exception1 = assertThrows(
                OperationNotAllowedException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("진행 중인 경매가 아닙니다.", exception1.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("비딩횟수 초과")
    public void createBidding_fail_bids_exceeded() {
        Long auctionId = TestData.TEST_AUCTION3.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 25000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        // 실제 DB에 10개의 비딩 추가
        for (int i = 0; i < 10; i++) {
            biddingRepository.save(Bidding.builder()
                    .auction(TestData.TEST_AUCTION3)
                    .member(TestData.TEST_MEMBER2)
                    .price(price + (i * 100L))
                    .build());
        }
        OperationNotAllowedException exception2 = assertThrows(
                OperationNotAllowedException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰은 10번까지만 가능합니다.", exception2.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("입찰단위에 맞지 않는 입찰")
    public void createBidding_fail_bid_not_fit_unit() {
        Long auctionId = TestData.TEST_AUCTION1.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 25100L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        DataInvalidException exception3 = assertThrows(DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("최소 입찰 금액 단위의 배수가 아닙니다.", exception3.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("현재가보다 낮은 입찰금액")
    public void createBidding_fail_lower_than_current_price() {
        Long auctionId = TestData.TEST_AUCTION1.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 20000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        DataInvalidException exception4 = assertThrows(DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰가가 현재가보다 작습니다.", exception4.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("최대호가보다 높은 입찰금액")
    public void createBidding_fail_higher_than_max_price() {
        Long auctionId = TestData.TEST_AUCTION1.getId();
        Long memberId = TestData.TEST_MEMBER2.getId();
        Long price = 100000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        DataInvalidException exception5 = assertThrows(DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰가가 1회 입찰 시 가능한 최대 금액(최소 입찰 금액 단위의 10배)을 초과하였습니다.",
                exception5.getErrorCode().getMessage());
    }
}
