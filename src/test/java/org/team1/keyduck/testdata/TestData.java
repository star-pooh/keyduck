package org.team1.keyduck.testdata;

import java.time.LocalDateTime;
import org.team1.keyduck.auction.entity.Auction;
import org.team1.keyduck.auction.entity.AuctionStatus;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.entity.MemberRole;

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
    public final static String TEST_NAME2 = "TestName1";
    public final static String TEST_EMAIL2 = "TestUser1@email.com";
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


    //AUCTION
    public final static Long TEST_AUCTION_ID1 = 1L;
    public final static String TEST_AUCTION_TITLE = "keyboard 1 auction";
    public final static Long TEST_AUCTION_START_PRICE1 = 50000L;
    public final static Long TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE1 = 500000L;
    public final static int TEST_AUCTION_BIDDING_UNIT1 = 5000;
    public final static LocalDateTime TEST_AUCTION_START_DATE1 = LocalDateTime.of(2025, 12, 25, 12,
            30);
    public final static LocalDateTime TEST_AUCTION_END_DATE1 = LocalDateTime.of(2025, 12, 30, 12,
            30);
    public final static Auction TEST_AUCTION1 = new Auction(TEST_KEYBOARD1, null,
            TEST_AUCTION_TITLE, TEST_AUCTION_START_PRICE1, TEST_AUCTION_IMMEDIATE_PURCHASE_PRICE1,
            TEST_AUCTION_START_PRICE1, TEST_AUCTION_BIDDING_UNIT1, TEST_AUCTION_START_DATE1,
            TEST_AUCTION_END_DATE1, AuctionStatus.NOT_STARTED);
}
