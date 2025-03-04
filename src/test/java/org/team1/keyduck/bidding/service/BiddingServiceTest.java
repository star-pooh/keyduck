package org.team1.keyduck.bidding.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.keyduck.auction.repository.AuctionRepository;
import org.team1.keyduck.auth.entity.AuthMember;
import org.team1.keyduck.bidding.dto.response.BiddingResponseDto;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.bidding.repository.BiddingRepository;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.OperationNotAllowedException;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.repository.MemberRepository;
import org.team1.keyduck.payment.repository.PaymentDepositRepository;
import org.team1.keyduck.payment.service.PaymentDepositService;
import org.team1.keyduck.testdata.TestData;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @BeforeEach
    public void setup() {
        setUpTestData();
    }

    private void setUpTestData() {
        memberRepository.save(TestData.TEST_MEMBER1);
        memberRepository.save(TestData.TEST_MEMBER2);
        memberRepository.save(TestData.TEST_MEMBER3);
        memberRepository.save(TestData.TEST_MEMBER4);
        keyboardRepository.save(TestData.TEST_KEYBOARD1);
        keyboardRepository.save(TestData.TEST_KEYBOARD2);
        keyboardRepository.save(TestData.TEST_KEYBOARD3);
        keyboardRepository.save(TestData.TEST_KEYBOARD4);
        keyboardRepository.save(TestData.TEST_KEYBOARD5);
        keyboardRepository.save(TestData.TEST_KEYBOARD6);
        keyboardRepository.save(TestData.TEST_KEYBOARD7);
        auctionRepository.save(TestData.TEST_AUCTION1);
        auctionRepository.save(TestData.TEST_AUCTION2);
        auctionRepository.save(TestData.TEST_AUCTION3);
        auctionRepository.save(TestData.TEST_AUCTION4);
        auctionRepository.save(TestData.TEST_AUCTION5);
        auctionRepository.save(TestData.TEST_AUCTION6);
        auctionRepository.save(TestData.TEST_AUCTION7);
        biddingRepository.save(TestData.TEST_BIDDING1);
        biddingRepository.save(TestData.TEST_BIDDING2);
        biddingRepository.save(TestData.TEST_BIDDING3);
        biddingRepository.save(TestData.TEST_BIDDING4);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT1);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT2);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT3);
        paymentDepositRepository.save(TestData.TEST_PAYMENT_DEPOSIT4);
    }

    //todo 성공케이스만들기

    @Test
    @DisplayName("진행중인 경매가 아닐때")
    public void createBidding_fail_Auction_Not_Inprogress() {
        Long auctionId = TestData.TEST_AUCTION2.getId();
        Long memberId = TestData.TEST_MEMBER4.getId();
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
        Long memberId = TestData.TEST_MEMBER4.getId();
        Long price = 25000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        // 실제 DB에 10개의 비딩 추가
        for (int i = 0; i < 10; i++) {
            biddingRepository.save(Bidding.builder()
                    .auction(TestData.TEST_AUCTION3)
                    .member(TestData.TEST_MEMBER4)
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
        Long memberId = TestData.TEST_MEMBER4.getId();
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
        Long memberId = TestData.TEST_MEMBER4.getId();
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
        Long memberId = TestData.TEST_MEMBER4.getId();
        Long price = 100000L;
        AuthMember authMember = new AuthMember(memberId, TestData.TEST_MEMBER_ROLE2);

        DataInvalidException exception5 = assertThrows(DataInvalidException.class,
                () -> biddingService.createBidding(auctionId, price, authMember)
        );
        assertEquals("입찰가가 1회 입찰 시 가능한 최대 금액(최소 입찰 금액 단위의 10배)을 초과하였습니다.",
                exception5.getErrorCode().getMessage());
    }

    @Test
    @DisplayName("입찰 조회")
    public void getbidding_by_auction() {
        Long auctionId = TestData.TEST_AUCTION1.getId();

        List<BiddingResponseDto> result = biddingService.getBiddingByAuction(auctionId);

        assertEquals(3, result.size());
        assertEquals(26000L, result.get(0).getBiddingPrice());
        assertEquals(24000L, result.get(1).getBiddingPrice());
        assertEquals(21000L, result.get(2).getBiddingPrice());
    }

    @Test
    @DisplayName("낙찰 조회")
    public void getbidding_success_bidding() {

    }
}

