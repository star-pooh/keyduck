package org.team1.keyduck.testdata;

import java.time.LocalDateTime;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.payment.entity.PaymentDeposit;

public class TestData {

    //MEMBER
    public final static Long TEST_ID1 = 1L;
    public final static String TEST_NAME1 = "TestName1";
    public final static String TEST_EMAIL1 = "TestUser1@email.com";
    public final static MemberRole TEST_MEMBER_ROLE1 = MemberRole.SELLER;
    public final static String TEST_CITY1 = "서울특별시";
    public final static String TEST_STATE1 = "강남구";
    public final static String TEST_STREET1 = "테헤란로";
    public final static String TEST_DETAIL_ADDRESS1 = "address1";
    public final static String TEST_DETAIL_ADDRESS2 = "address2";
    public final static Address TEST_ADDRESS1 = new Address(TEST_CITY1, TEST_STATE1, TEST_STREET1,
            TEST_DETAIL_ADDRESS1, TEST_DETAIL_ADDRESS2);
    public final static String TEST_PASSWORD1 = "Password123!";
    public final static Member TEST_MEMBER1 = new Member(TEST_NAME1, TEST_EMAIL1, TEST_PASSWORD1,
            TEST_MEMBER_ROLE1, TEST_ADDRESS1);

    public final static Long TEST_ID2 = 2L;
    public final static String TEST_NAME2 = "TestName2";
    public final static String TEST_EMAIL2 = "TestUser2@email.com";
    public final static MemberRole TEST_MEMBER_ROLE2 = MemberRole.CUSTOMER;
    public final static String TEST_CITY2 = "서울특별시";
    public final static String TEST_STATE2 = "강남구";
    public final static String TEST_STREET2 = "테헤란로";
    public final static String TEST_DETAIL_ADDRESS3 = "address1";
    public final static String TEST_DETAIL_ADDRESS4 = "address2";
    public final static Address TEST_ADDRESS2 = new Address(TEST_CITY2, TEST_STATE2, TEST_STREET2,
            TEST_DETAIL_ADDRESS3, TEST_DETAIL_ADDRESS4);
    public final static String TEST_PASSWORD2 = "Password123!";
    public final static Member TEST_MEMBER2 = new Member(TEST_NAME2, TEST_EMAIL2, TEST_PASSWORD2,
            TEST_MEMBER_ROLE2, TEST_ADDRESS2);

    public final static Long TEST_ID3 = 3L;
    public final static String TEST_NAME3 = "TestName3";
    public final static String TEST_EMAIL3 = "TestUser3@email.com";
    public final static MemberRole TEST_MEMBER_ROLE3 = MemberRole.CUSTOMER;
    public final static Address TEST_ADDRESS3 = new Address("서울특별시", "서초구", "반포대로", "address5",
            "address6");
    public final static String TEST_PASSWORD3 = "Password123!";
    public final static Member TEST_MEMBER3 = new Member(TEST_NAME3, TEST_EMAIL3, TEST_PASSWORD3,
            TEST_MEMBER_ROLE3, TEST_ADDRESS3);

    public final static Long TEST_ID4 = 4L;
    public final static String TEST_NAME4 = "TestName2";
    public final static String TEST_EMAIL4 = "TestUser4@email.com";
    public final static MemberRole TEST_MEMBER_ROLE4 = MemberRole.CUSTOMER;
    public final static Address TEST_ADDRESS4 = new Address(TEST_CITY2, TEST_STATE2, TEST_STREET2,
            TEST_DETAIL_ADDRESS3, TEST_DETAIL_ADDRESS4);
    public final static String TEST_PASSWORD4 = "Password123!";
    public final static Member TEST_MEMBER4 = new Member(TEST_NAME4, TEST_EMAIL4, TEST_PASSWORD4,
            TEST_MEMBER_ROLE4, TEST_ADDRESS4);


    //KEYBOARD
    public final static Long TEST_KEYBOARD_ID1 = 1L;
    public final static String TEST_KEYBOARD_NAME1 = "keyboard";
    public final static String TEST_KEYBOARD_DESCRIPTION1 = "짱짱맨";
    public final static Keyboard TEST_KEYBOARD1 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME1,
            TEST_KEYBOARD_DESCRIPTION1);

    public final static Long TEST_KEYBOARD_ID2 = 2L;
    public final static String TEST_KEYBOARD_NAME2 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION2 = "짱짱걸";
    public final static Keyboard TEST_KEYBOARD2 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME2,
            TEST_KEYBOARD_DESCRIPTION2);

    public final static Long TEST_KEYBOARD_ID3 = 3L;
    public final static String TEST_KEYBOARD_NAME3 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION3 = "짱짱";
    public final static Keyboard TEST_KEYBOARD3 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME3,
            TEST_KEYBOARD_DESCRIPTION3);

    public final static Long TEST_KEYBOARD_ID4 = 4L;
    public final static String TEST_KEYBOARD_NAME4 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION4 = "짱짱";
    public final static Keyboard TEST_KEYBOARD4 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME4,
            TEST_KEYBOARD_DESCRIPTION4);
    public final static Long TEST_KEYBOARD_ID5 = 5L;
    public final static String TEST_KEYBOARD_NAME5 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION5 = "짱짱";
    public final static Keyboard TEST_KEYBOARD5 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME5,
            TEST_KEYBOARD_DESCRIPTION5);
    public final static Long TEST_KEYBOARD_ID6 = 6L;
    public final static String TEST_KEYBOARD_NAME6 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION6 = "짱짱";
    public final static Keyboard TEST_KEYBOARD6 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME6,
            TEST_KEYBOARD_DESCRIPTION6);
    public final static Long TEST_KEYBOARD_ID7 = 7L;
    public final static String TEST_KEYBOARD_NAME7 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION7 = "짱짱";
    public final static Keyboard TEST_KEYBOARD7 = new Keyboard(TEST_MEMBER1, TEST_KEYBOARD_NAME7,
            TEST_KEYBOARD_DESCRIPTION7);

    //auction
    public final static Long TEST_AUCTION_ID1 = 1L;
    public final static String TEST_AUCTION_TITLE1 = "Keyboard_auction1";
    public final static Long START_PRICE1 = 20000L;
    public final static Long BIDDING_UNIT1 = 1000L;
    public final static Long CURRENT_PRICE1 = 21000L;
    public final static LocalDateTime START_DATE1 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE1 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS1 = AuctionStatus.IN_PROGRESS;
    public final static Auction TEST_AUCTION1 = Auction.builder()
            .keyboard(TEST_KEYBOARD1)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE1)
            .startPrice(START_PRICE1)
            .currentPrice(CURRENT_PRICE1)
            .biddingUnit(BIDDING_UNIT1)
            .auctionStartDate(START_DATE1)
            .auctionEndDate(END_DATE1)
            .auctionStatus(AUCTION_STATUS1)
            .build();

    public final static Long TEST_AUCTION_ID2 = 2L;
    public final static Long KEYBOARD_ID2 = 2L;
    public final static String TEST_AUCTION_TITLE2 = "Keyboard_auction2";
    public final static Long START_PRICE2 = 40000L;
    public final static Long BIDDING_UNIT2 = 1000L;
    public final static LocalDateTime START_DATE2 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE2 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS2 = AuctionStatus.NOT_STARTED;
    public final static Auction TEST_AUCTION2 = Auction.builder()
            .keyboard(TEST_KEYBOARD2)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE2)
            .startPrice(START_PRICE2)
            .currentPrice(START_PRICE2)
            .biddingUnit(BIDDING_UNIT2)
            .auctionStartDate(START_DATE2)
            .auctionEndDate(END_DATE2)
            .auctionStatus(AUCTION_STATUS2)
            .build();


    public final static Long TEST_AUCTION_ID3 = 3L;
    public final static Long KEYBOARD_ID3 = 3L;
    public final static String TEST_AUCTION_TITLE3 = "Keyboard_auction3";
    public final static Long START_PRICE3 = 20000L;
    public final static Long BIDDING_UNIT3 = 10000L;
    public final static Long CURRENT_PRICE3 = 40000L;
    public final static LocalDateTime START_DATE3 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE3 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS3 = AuctionStatus.IN_PROGRESS;
    public final static Auction TEST_AUCTION3 = Auction.builder()
            .keyboard(TEST_KEYBOARD3)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE3)
            .startPrice(START_PRICE3)
            .currentPrice(CURRENT_PRICE3)
            .biddingUnit(BIDDING_UNIT3)
            .auctionStartDate(START_DATE3)
            .auctionEndDate(END_DATE3)
            .auctionStatus(AUCTION_STATUS3)
            .build();

    public final static Long TEST_AUCTION_ID4 = 4L;
    public final static Long KEYBOARD_ID4 = 4L;
    public final static String TEST_AUCTION_TITLE4 = "Keyboard_auction4";
    public final static Long START_PRICE4 = 20000L;
    public final static Long BIDDING_UNIT4 = 10000L;
    public final static Long CURRENT_PRICE4 = 40000L;
    public final static LocalDateTime START_DATE4 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE4 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS4 = AuctionStatus.IN_PROGRESS;
    public final static Auction TEST_AUCTION4 = Auction.builder()
            .keyboard(TEST_KEYBOARD4)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE4)
            .startPrice(START_PRICE4)
            .currentPrice(CURRENT_PRICE4)
            .biddingUnit(BIDDING_UNIT4)
            .auctionStartDate(START_DATE4)
            .auctionEndDate(END_DATE4)
            .auctionStatus(AUCTION_STATUS4)
            .build();

    public final static Long TEST_AUCTION_ID5 = 5L;
    public final static Long KEYBOARD_ID5 = 5L;
    public final static String TEST_AUCTION_TITLE5 = "Keyboard_auction5";
    public final static Long START_PRICE5 = 20000L;
    public final static Long BIDDING_UNIT5 = 10000L;
    public final static Long CURRENT_PRICE5 = 40000L;
    public final static LocalDateTime START_DATE5 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE5 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS5 = AuctionStatus.CLOSED;
    public final static Auction TEST_AUCTION5 = Auction.builder()
            .keyboard(TEST_KEYBOARD5)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE5)
            .startPrice(START_PRICE5)
            .currentPrice(CURRENT_PRICE5)
            .biddingUnit(BIDDING_UNIT5)
            .auctionStartDate(START_DATE5)
            .auctionEndDate(END_DATE5)
            .auctionStatus(AUCTION_STATUS5)
            .build();

    public final static Long TEST_AUCTION_ID6 = 6L;
    public final static Long KEYBOARD_ID6 = 5L;
    public final static String TEST_AUCTION_TITLE6 = "Keyboard_auction6";
    public final static Long START_PRICE6 = 20000L;
    public final static Long BIDDING_UNIT6 = 10000L;
    public final static Long CURRENT_PRICE6 = 40000L;
    public final static LocalDateTime START_DATE6 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE6 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS6 = AuctionStatus.CLOSED;
    public final static Auction TEST_AUCTION6 = Auction.builder()
            .keyboard(TEST_KEYBOARD6)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE6)
            .startPrice(START_PRICE6)
            .currentPrice(CURRENT_PRICE6)
            .biddingUnit(BIDDING_UNIT6)
            .auctionStartDate(START_DATE6)
            .auctionEndDate(END_DATE6)
            .auctionStatus(AUCTION_STATUS6)
            .build();

    public final static Long TEST_AUCTION_ID7 = 6L;
    public final static Long KEYBOARD_ID7 = 5L;
    public final static String TEST_AUCTION_TITLE7 = "Keyboard_auction6";
    public final static Long START_PRICE7 = 20000L;
    public final static Long BIDDING_UNIT7 = 10000L;
    public final static Long CURRENT_PRICE7 = 40000L;
    public final static LocalDateTime START_DATE7 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE7 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS7 = AuctionStatus.CLOSED;
    public final static Auction TEST_AUCTION7 = Auction.builder()
            .keyboard(TEST_KEYBOARD7)
            .member(TEST_MEMBER1)
            .title(TEST_AUCTION_TITLE7)
            .startPrice(START_PRICE7)
            .currentPrice(CURRENT_PRICE7)
            .biddingUnit(BIDDING_UNIT7)
            .auctionStartDate(START_DATE7)
            .auctionEndDate(END_DATE7)
            .auctionStatus(AUCTION_STATUS7)
            .build();

    //BIDDING
    public final static Long TEST_BIDDING_ID1 = 1L;
    public final static Long MY_GREAT_PRICE1 = 21000L;
    public final static Bidding TEST_BIDDING1 = Bidding.builder()
            .auction(TEST_AUCTION1)
            .member(TEST_MEMBER2)
            .price(MY_GREAT_PRICE1)
            .build();
    public final static Long TEST_BIDDING_ID2 = 2L;
    public final static Long MY_GREAT_PRICE2 = 26000L;
    public final static Bidding TEST_BIDDING2 = Bidding.builder()
            .auction(TEST_AUCTION1)
            .member(TEST_MEMBER4)
            .price(MY_GREAT_PRICE2)
            .build();
    public final static Long TEST_BIDDING_ID3 = 3L;
    public final static Long MY_GREAT_PRICE3 = 24000L;
    public final static Bidding TEST_BIDDING3 = Bidding.builder()
            .auction(TEST_AUCTION3)
            .member(TEST_MEMBER3)
            .price(MY_GREAT_PRICE2)
            .build();
    public final static Long TEST_BIDDING_ID4 = 4L;
    public final static Long MY_GREAT_PRICE4 = 24000L;
    public final static Bidding TEST_BIDDING4 = Bidding.builder()
            .auction(TEST_AUCTION1)
            .member(TEST_MEMBER3)
            .price(MY_GREAT_PRICE4)
            .build();


    //paymentdeposit
    public final static Long DEPOSIT_AMOUNT1 = 500000000L;
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT3 = PaymentDeposit.builder()
            .member(TEST_MEMBER3)
            .depositAmount(DEPOSIT_AMOUNT1)
            .build();
    public final static Long DEPOSIT_AMOUNT2 = 500000000L;
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT4 = PaymentDeposit.builder()
            .member(TEST_MEMBER4)
            .depositAmount(DEPOSIT_AMOUNT2)
            .build();
    public final static Long TEST_DEPOSIT_AMOUNT = 5000L;
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT1 = PaymentDeposit.builder()
            .member(TestData.TEST_MEMBER1)
            .depositAmount(TestData.TEST_DEPOSIT_AMOUNT)
            .build();
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT2 = PaymentDeposit.builder()
            .member(TestData.TEST_MEMBER2)
            .depositAmount(TestData.TEST_DEPOSIT_AMOUNT)
            .build();

    //token
    public final static String TEST_TOKEN = "TEST";

}