package org.team1.keyduck.bidding.service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
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
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.testdata.TestData;

@SpringBootTest
@ActiveProfiles("test")
public class BiddingServiceTest {
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BiddingServiceTest {

    @Autowired
    private BiddingService biddingService;
    @Autowired
    private BiddingRepository biddingRepository;
    @Autowired
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private AuctionRepository auctionRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private KeyboardRepository keyboardRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
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

    private Member member;
    private Auction auction;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

    private static final Logger log = LoggerFactory.getLogger(BiddingServiceTest.class);
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

    public void setUpBidding() {
        Auction auction = TestData.TEST_AUCTION1; //  실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

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
        when(biddingRepository.findByMember_IdAndAuction_Id(memberId, auctionId)).thenReturn(
                Optional.empty());

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
        Long myGreatBidding = 0L;

        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();
        ReflectionTestUtils.setField(bidding, "id", 2L);
        when(biddingRepository.save(any(Bidding.class))).thenReturn(bidding);

        List<Member> customers = memberRepository.findAll().stream()
                .filter(m -> m.getMemberRole() == MemberRole.CUSTOMER)
                .toList();
        doNothing().when(paymentDepositService).payBiddingPrice(anyLong(), anyLong(), anyLong());

        String depositSql = "INSERT INTO payment_deposit (member_id, deposit_amount) VALUES (?, ?)";
        List<Object[]> paymentDeposits = new ArrayList<>();
        // when
        biddingService.createBidding(auctionId, price, authMember);

        for (Member customer : customers) {
            paymentDeposits.add(new Object[]{customer.getId(), 1_000_000L});
        }
        // then
        assertEquals(price, auction.getCurrentPrice()); //  현재 가격이 업데이트되었는지 검증
        assertEquals(price, bidding.getPrice());
        assertNotNull(bidding);

        jdbcTemplate.batchUpdate(depositSql, paymentDeposits, BATCH_SIZE,
                (ps, param) -> {
                    ps.setLong(1, (Long) param[0]);
                    ps.setLong(2, (Long) param[1]);
                });
        verify(paymentDepositService, times(1)).payBiddingPrice(memberId, price, myGreatBidding);
        verify(biddingRepository, times(1)).save(any(Bidding.class));
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
    void createBidding_success_두번째이상() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID1;
        Long memberId = TestData.TEST_ID2;
        Long price = 25000L; //  새로운 입찰가
        Long myGreatBidding = TestData.TEST_AUCTION1.getCurrentPrice(); //  기존 입찰가

        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        for (Member member : members) {
            if (member.getMemberRole() == MemberRole.CUSTOMER) {
                customers.add(member);
            }
        }
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch latch = new CountDownLatch(100);
        Auction auction = TestData.TEST_AUCTION1; //  실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        for (int i = 0; i < 100; i++) {
            final long biddingPrice = auction.getCurrentPrice() + (i + 1) * 100L;
            final Member customer = customers.get(i);
        when(biddingRepository.findByMember_IdAndAuction_Id(memberId, auctionId))
                .thenReturn(Optional.of(myGreatBidding)); //  기존 입찰 내역 존재

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
        Bidding bidding = Bidding.builder()
                .auction(auction)
                .member(member)
                .price(price)
                .build();
        ReflectionTestUtils.setField(bidding, "id", 2L);
        when(biddingRepository.save(any(Bidding.class))).thenReturn(bidding);

        latch.await();
        executorService.shutdown();
        doNothing().when(paymentDepositService).payBiddingPrice(anyLong(), anyLong(), anyLong());

        // when
        biddingService.createBidding(auctionId, price, authMember);

        // then
        // 최신 현재가
        Auction updatedAuction = auctionRepository.findById(auction.getId()).get();
        assertEquals(price, auction.getCurrentPrice()); //  현재 가격이 업데이트되었는지 검증
        assertEquals(price, bidding.getPrice());
        assertNotNull(bidding);

        //  기존 입찰가가 `payBiddingPrice()`에서 정상적으로 반영되었는지 검증
        verify(paymentDepositService, times(1)).payBiddingPrice(memberId, price, myGreatBidding);

        //  새로운 입찰가로 저장되었는지 검증
        verify(biddingRepository, times(1)).save(any(Bidding.class));
    }


    @Test
    void createBidding_실패1_경매가_진행중이_아님() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID2; //  진행 중이 아닌 경매 사용
        Long memberId = TestData.TEST_ID2;
        Long price = 40000L;

        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION2; //  실제 객체 사용
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // when, then
        Exception exception = assertThrows(
                OperationNotAllowedException.class, //  예외 타입 검증
                () -> biddingService.createBidding(auctionId, price, authMember) //  실행
        );

        //  예외 메시지 검증
        assertEquals(ErrorCode.AUCTION_NOT_IN_PROGRESS,
                ((OperationNotAllowedException) exception).getErrorCode());
    }

        // 최종 입찰가
        Bidding lastBidding = biddingRepository.findByAuctionIdOrderByPriceDesc(auction.getId())
                .get(0);
        assertEquals(lastBidding.getPrice(), updatedAuction.getCurrentPrice());
    @Test
    void createBidding_실패2_비딩_횟수가_10번이상() {
        // given
        Long auctionId = TestData.TEST_AUCTION_ID1;
        Long price = 30000L;
        Long memberId = TestData.TEST_ID2;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);

        //mock
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION1;
        ReflectionTestUtils.setField(auction, "id", auctionId);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // 비딩 횟수 초과
        when(biddingRepository.countByMember_IdAndAuction_Id(anyLong(), anyLong())).thenReturn(10L);

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
        Long auctionId = TestData.TEST_AUCTION_ID3;
        ;
        Long price = 45000L;
        Long memberId = TestData.TEST_ID2;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION3;
        ReflectionTestUtils.setField(auction, "id", auctionId);
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
        Long auctionId = TestData.TEST_AUCTION_ID3;
        Long price = 30000L;
        Long memberId = TestData.TEST_ID2;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION3;
        ReflectionTestUtils.setField(auction, "id", auctionId);
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
        Long auctionId = TestData.TEST_AUCTION_ID1;
        Long price = 100000000L;
        Long memberId = TestData.TEST_ID2;

        AuthMember authMember = new AuthMember(memberId, MemberRole.CUSTOMER);
        //mock
        Member member = TestData.TEST_MEMBER2;
        ReflectionTestUtils.setField(member, "id", memberId);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        Auction auction = TestData.TEST_AUCTION1;
        ReflectionTestUtils.setField(auction, "id", auctionId);
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