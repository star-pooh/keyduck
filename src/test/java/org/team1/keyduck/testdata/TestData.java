package org.team1.keyduck.testdata;

import java.time.LocalDateTime;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.bidding.entity.Bidding;
import java.time.LocalDateTime;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
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

    public final static String TEST_PASSWORD3 = "Qwer123@";


    //KEYBOARD
    public final static Long TEST_KEYBOARD_ID1 = 1L;
    public final static String TEST_KEYBOARD_NAME1 = "keyboard";
    public final static String TEST_KEYBOARD_DESCRIPTION1 = "짱짱맨";
    public final static Keyboard TEST_KEYBOARD1 = Keyboard.builder()
            .member(TEST_MEMBER1)
            .name(TEST_KEYBOARD_NAME1)
            .description(TEST_KEYBOARD_DESCRIPTION1)
            .build();

    public final static Long TEST_KEYBOARD_ID2 = 2L;
    public final static String TEST_KEYBOARD_NAME2 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION2 = "짱짱걸";
    public final static Keyboard TEST_KEYBOARD2 = Keyboard.builder()
            .member(TEST_MEMBER1)
            .name(TEST_KEYBOARD_NAME2)
            .description(TEST_KEYBOARD_DESCRIPTION2)
            .build();

    public final static Long TEST_KEYBOARD_ID3 = 3L;
    public final static String TEST_KEYBOARD_NAME3 = "keyboard!!!";
    public final static String TEST_KEYBOARD_DESCRIPTION3 = "짱짱";
    public final static Keyboard TEST_KEYBOARD3 = Keyboard.builder()
            .member(TEST_MEMBER1)
            .name(TEST_KEYBOARD_NAME3)
            .description(TEST_KEYBOARD_DESCRIPTION3)
            .build();

    // TEMP PAYMENT
    public final static String TEST_ORDER_ID1 = "orderId1";
    public final static Long TEST_PAYMENT_AMOUNT1 = 1000L;
    public final static Long TEST_PAYMENT_AMOUNT2 = 2000L;
    public final static TempPayment TEST_TEMP_PAYMENT1 = TempPayment.builder()
            .memberId(TestData.TEST_ID1)
            .orderId(TestData.TEST_ORDER_ID1)
            .amount(TestData.TEST_PAYMENT_AMOUNT1)
            .build();

    // PAYMENT
    public final static PaymentMethod TEST_PAYMENT_METHOD = PaymentMethod.EASY_PAY;
    public final static String TEST_EASY_PAY_TYPE = "카카오페이";
    public final static PaymentStatus TEST_PAYMENT_STATUS = PaymentStatus.DONE;
    public final static LocalDateTime TEST_REQUESTED_AT =
            LocalDateTime.parse("2025-02-24T22:52:37");
    public final static LocalDateTime TEST_APPROVED_AT =
            LocalDateTime.parse("2025-02-24T22:55:44");
    public final static Payment TEST_PAYMENT1 = Payment.builder()
            .member(TEST_MEMBER1)
            .orderId(TEST_ORDER_ID1)
            .amount(TEST_PAYMENT_AMOUNT1)
            .paymentMethod(TEST_PAYMENT_METHOD)
            .easyPayType(TEST_EASY_PAY_TYPE)
            .paymentStatus(TEST_PAYMENT_STATUS)
            .requestedAt(TEST_REQUESTED_AT)
            .approvedAt(TEST_APPROVED_AT)
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

    //AUCTION
    public final static Long TEST_AUCTION_ID1 = 1L;
    public final static String TEST_AUCTION_TITLE = "keyboard 1 auction";
    public final static Long TEST_AUCTION_START_PRICE1 = 50000L;
    public final static Long TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE1 = 500000L;
    public final static Long TEST_AUCTION_BIDDING_UNIT1 = 5000L;
    public final static LocalDateTime TEST_AUCTION_START_DATE1 = LocalDateTime.now().plusDays(1);
    public final static LocalDateTime TEST_AUCTION_END_DATE1 = LocalDateTime.now().plusDays(3);
    public final static Auction TEST_AUCTION1 = Auction.builder()
            .keyboard(TEST_KEYBOARD1)
            .member(null)
            .title(TEST_AUCTION_TITLE)
            .startPrice(TEST_AUCTION_START_PRICE1)
            .immediatePurchasePrice(TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE1)
            .currentPrice(TEST_AUCTION_START_PRICE1)
            .biddingUnit(TEST_AUCTION_BIDDING_UNIT1)
            .auctionStartDate(TEST_AUCTION_START_DATE1)
            .auctionEndDate(TEST_AUCTION_END_DATE1)
            .auctionStatus(AuctionStatus.NOT_STARTED)
            .build();

    public final static Long TEST_AUCTION_ID2 = 2L;
    public final static String TEST_AUCTION_TITLE2 = "keyboard 2 auction";
    public final static Long TEST_AUCTION_START_PRICE2 = 30000L;
    public final static Long TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE2 = 300000L;
    public final static Long TEST_AUCTION_BIDDING_UNIT2 = 5000L;
    public final static LocalDateTime TEST_AUCTION_START_DATE2 = LocalDateTime.now().plusDays(1);
    public final static LocalDateTime TEST_AUCTION_END_DATE2 = LocalDateTime.now().plusDays(3);
    public final static Auction TEST_AUCTION2 = Auction.builder()
            .keyboard(TEST_KEYBOARD2)
            .member(null)
            .title(TEST_AUCTION_TITLE2)
            .startPrice(TEST_AUCTION_START_PRICE2)
            .immediatePurchasePrice(TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE2)
            .currentPrice(TEST_AUCTION_START_PRICE2)
            .biddingUnit(TEST_AUCTION_BIDDING_UNIT2)
            .auctionStartDate(TEST_AUCTION_START_DATE2)
            .auctionEndDate(TEST_AUCTION_END_DATE2)
            .auctionStatus(AuctionStatus.NOT_STARTED)
            .build();

    //auction
    public final static Long TEST_AUCTION_ID1 = 1L;
    public final static String TEST_AUCTION_TITLE1 = "Keyboard_auction1";
    public final static Long START_PRICE1 = 20000L;
    public final static int BIDDING_UNIT1 = 1000;
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
    public final static int BIDDING_UNIT2 = 1000;
    public final static LocalDateTime START_DATE2 = LocalDateTime.now().plusHours(1);
    public final static LocalDateTime END_DATE2 = LocalDateTime.now().plusDays(1);
    public final static AuctionStatus AUCTION_STATUS2 = AuctionStatus.NOT_STARTED;
    public final static Auction TEST_AUCTION2 = Auction.builder()
            .keyboard(TEST_KEYBOARD1)
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
    public final static int BIDDING_UNIT3 = 10000;
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

    //BIDDING
    public final static Long TEST_BIDDING_ID1 = 1L;
    public final static Long MY_GREAT_PRICE1 = 21000L;
    public final static Bidding TEST_BIDDING1 = Bidding.builder()
            .auction(TEST_AUCTION1)
            .member(TEST_MEMBER2)
            .price(MY_GREAT_PRICE1)
            .build();

    //paymentdeposit
    public final static Long DEPOSIT_AMOUNT1 = 50000L;
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT = PaymentDeposit.builder()
            .member(TEST_MEMBER2)
            .depositAmount(DEPOSIT_AMOUNT1)
            .build();
}