package org.team1.keyduck.bidding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
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


    private Member member;
    private Auction auction;

    private static final Logger log = LoggerFactory.getLogger(BiddingServiceTest.class);

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
                        .biddingUnit(100L)
                        .auctionStartDate(LocalDateTime.now())
                        .auctionEndDate(LocalDateTime.now().plusDays(1))
                        .auctionStatus(AuctionStatus.IN_PROGRESS)
                        .build()
        );

        Faker faker = new Faker();
        final int MAX_CUSTOMERS = 100;
        final int BATCH_SIZE = 100;
        final String PASSWORD = "5678";

        String memberSql = "INSERT INTO member (name, email, password, member_role, city, state, street, detail_address1, detail_address2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        List<Object[]> members = new ArrayList<>();

        for (int i = 0; i < MAX_CUSTOMERS; i++) {
            members.add(new Object[]{
                    "구매자" + i,
                    faker.color().name() + i + "@" + faker.animal() + ".com",
                    PASSWORD,
                    MemberRole.CUSTOMER.name(),
                    "경기도", "안양시", "만안구", "56789", "2층"
            });
        }

        jdbcTemplate.batchUpdate(memberSql, members, BATCH_SIZE,
                (ps, param) -> {
                    ps.setString(1, (String) param[0]);
                    ps.setString(2, (String) param[1]);
                    ps.setString(3, (String) param[2]);
                    ps.setString(4, (String) param[3]);
                    ps.setString(5, (String) param[4]);
                    ps.setString(6, (String) param[5]);
                    ps.setString(7, (String) param[6]);
                    ps.setString(8, (String) param[7]);
                    ps.setString(9, (String) param[8]);
                });

        List<Member> customers = memberRepository.findAll().stream()
                .filter(m -> m.getMemberRole() == MemberRole.CUSTOMER)
                .toList();

        String depositSql = "INSERT INTO payment_deposit (member_id, deposit_amount) VALUES (?, ?)";
        List<Object[]> paymentDeposits = new ArrayList<>();

        for (Member customer : customers) {
            paymentDeposits.add(new Object[]{customer.getId(), 1_000_000L});
        }

        jdbcTemplate.batchUpdate(depositSql, paymentDeposits, BATCH_SIZE,
                (ps, param) -> {
                    ps.setLong(1, (Long) param[0]);
                    ps.setLong(2, (Long) param[1]);
                });
    }

    @BeforeEach
    public void setUp() {
        setUpBidding();
    }


    @Test
    @DisplayName("비관적 락 이용한 입찰 생성")
    public void createBiddingWithPessimisticLock() throws InterruptedException {
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
                    biddingService.createBidding(
                            auction.getId(),
                            biddingPrice,
                            new AuthMember(customer.getId(), customer.getMemberRole())
                    );
                } catch (DataInvalidException e) {
                    log.info("입찰 실패 - 유저 ID: {}, 현재가: {}, 입찰 금액: {}", customer.getId(),
                            auction.getCurrentPrice(), biddingPrice);
                    log.info("예외 메시지: {}", e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        // 최신 현재가
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();

        // 최종 입찰가
        Bidding lastBidding = biddingRepository.findByAuctionIdOrderByPriceDesc(auction.getId())
                .get(0);
        assertEquals(lastBidding.getPrice(), updatedAuction.getCurrentPrice());

    }
}