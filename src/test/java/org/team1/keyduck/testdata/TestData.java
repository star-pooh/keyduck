package org.team1.keyduck.testdata;

import java.time.LocalDateTime;
import java.util.List;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.bidding.entity.Bidding;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;
import org.team1.keyduck.payment.entity.Payment;
import org.team1.keyduck.payment.entity.PaymentDeposit;
import org.team1.keyduck.payment.entity.PaymentMethod;
import org.team1.keyduck.payment.entity.PaymentStatus;
import org.team1.keyduck.payment.entity.TempPayment;

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
    public final static String TEST_NAME3 = "TestName2";
    public final static String TEST_EMAIL3 = "TestUser2@email.com";
    public final static MemberRole TEST_MEMBER_ROLE3 = MemberRole.CUSTOMER;
    public final static String TEST_CITY3 = "서울특별시";
    public final static String TEST_STATE3 = "강남구";
    public final static String TEST_STREET3 = "테헤란로";
    public final static String TEST_DETAIL_ADDRESS5 = "address1";
    public final static String TEST_DETAIL_ADDRESS6 = "address2";
    public final static Address TEST_ADDRESS3 = new Address(TEST_CITY2, TEST_STATE2, TEST_STREET2,
            TEST_DETAIL_ADDRESS3, TEST_DETAIL_ADDRESS4);
    public final static String TEST_PASSWORD4 = "Password123!";
    public final static Member TEST_MEMBER3 = new Member(TEST_NAME3, TEST_EMAIL3, TEST_PASSWORD4,
            TEST_MEMBER_ROLE3, TEST_ADDRESS3);
    public final static String TEST_PASSWORD3 = "Qwer123@";

    public final static Long TEST_ID4 = 4L;
    public final static String TEST_NAME4 = "TestName2";
    public final static String TEST_EMAIL4 = "TestUser4@email.com";
    public final static MemberRole TEST_MEMBER_ROLE4 = MemberRole.CUSTOMER;
    public final static Address TEST_ADDRESS4 = new Address(TEST_CITY2, TEST_STATE2, TEST_STREET2,
            TEST_DETAIL_ADDRESS3, TEST_DETAIL_ADDRESS4);
    public final static Member TEST_MEMBER4 = new Member(TEST_NAME4, TEST_EMAIL4, TEST_PASSWORD4,
            TEST_MEMBER_ROLE4, TEST_ADDRESS4);


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
            .memberId(TEST_ID1)
            .orderId(TEST_ORDER_ID1)
            .amount(TEST_PAYMENT_AMOUNT1)
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
    public final static Long TEST_AUCTION_ID3 = 1L;
    public final static Long START_PRICE1 = 20000L;
    public final static Long BIDDING_UNIT1 = 1000L;
    public final static Long CURRENT_PRICE1 = 40000L;
    public final static Long IMMEDIATE_PURCHASE_PRICE1 = 100000L;
    public final static AuctionStatus AUCTION_STATUS1 = AuctionStatus.IN_PROGRESS;
    public final static AuctionStatus AUCTION_STATUS2 = AuctionStatus.NOT_STARTED;
    public final static AuctionStatus AUCTION_STATUS3 = AuctionStatus.CLOSED;
    public final static Auction TEST_AUCTION3 = Auction.builder()
            .keyboard(TEST_KEYBOARD1).startPrice(START_PRICE1).currentPrice(START_PRICE1)
            .biddingUnit(BIDDING_UNIT1).auctionStatus(AUCTION_STATUS1).build();

    public final static Long TEST_AUCTION_ID4 = 2L;
    public final static Auction TEST_AUCTION4 = Auction.builder()
            .keyboard(TEST_KEYBOARD2).startPrice(START_PRICE1).currentPrice(START_PRICE1)
            .biddingUnit(BIDDING_UNIT1).auctionStatus(AUCTION_STATUS1).build();

    public final static Long TEST_AUCTION_ID5 = 3L;
    public final static Auction TEST_AUCTION5 = Auction.builder()
            .keyboard(TEST_KEYBOARD3).startPrice(START_PRICE1).currentPrice(CURRENT_PRICE1)
            .biddingUnit(BIDDING_UNIT1).immediatePurchasePrice(IMMEDIATE_PURCHASE_PRICE1)
            .auctionStatus(AUCTION_STATUS1).build();

    public final static Long TEST_AUCTION_ID6 = 4L;
    public final static Auction TEST_AUCTION6 = Auction.builder()
            .keyboard(TEST_KEYBOARD4).startPrice(START_PRICE1).currentPrice(CURRENT_PRICE1)
            .biddingUnit(BIDDING_UNIT1).auctionStatus(AUCTION_STATUS2).build();

    public final static Long TEST_AUCTION_ID7 = 5L;
    public final static Auction TEST_AUCTION7 = Auction.builder()
            .keyboard(TEST_KEYBOARD5).startPrice(START_PRICE1).currentPrice(CURRENT_PRICE1)
            .biddingUnit(BIDDING_UNIT1).auctionStatus(AUCTION_STATUS1).build();

    public final static Long TEST_AUCTION_ID8 = 6L;
    public final static Auction TEST_AUCTION8 = Auction.builder()
            .keyboard(TEST_KEYBOARD6).startPrice(START_PRICE1).currentPrice(CURRENT_PRICE1)
            .biddingUnit(BIDDING_UNIT1).auctionStatus(AUCTION_STATUS1).build();


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
            .member(TEST_MEMBER1)
            .depositAmount(TEST_DEPOSIT_AMOUNT)
            .build();
    public final static PaymentDeposit TEST_PAYMENT_DEPOSIT2 = PaymentDeposit.builder()
            .member(TEST_MEMBER2)
            .depositAmount(TEST_DEPOSIT_AMOUNT)
            .build();
    //Bidding
    public final static Long TEST_BIDDING_ID1 = 1L;
    public final static Long TEST_BIDDING_PRICE1 = 60000L;
    public final static LocalDateTime TEST_BIDDING_CREATED_AT1 = LocalDateTime.now().minusDays(1);
    public final static Bidding TEST_BIDDING1 = new Bidding(TEST_AUCTION1, TEST_MEMBER2,
            TEST_BIDDING_PRICE1);

    public final static Long TEST_BIDDING_ID2 = 2L;
    public final static Long TEST_BIDDING_PRICE2 = 70000L;
    public final static LocalDateTime TEST_BIDDING_CREATED_AT2 = LocalDateTime.now().minusHours(3);
    public final static Bidding TEST_BIDDING2 = new Bidding(TEST_AUCTION1, TEST_MEMBER3,
            TEST_BIDDING_PRICE2);

    public final static List<Bidding> TEST_BIDDINGS = List.of(TEST_BIDDING1, TEST_BIDDING2);


    //token
    public final static String TEST_TOKEN = "TEST";

}