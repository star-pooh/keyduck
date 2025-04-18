package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class ValidationErrorMessage {

    // 멤버
    public static final String NAME_IS_NOT_NULL = "이름은 필수 입력 값입니다.";

    public static final String EMAIL_IS_NOT_NULL = "이메일은 필수 입력 값입니다.";
    public static final String EMAIL_IS_NOT_VALID = "유효하지 않은 이메일 입니다.";

    public static final String PASSWORD_IS_NOT_NULL = "비밀번호는 필수 입력 값입니다.";
    public static final String PASSWORD_IS_NOT_AVAILABLE = "유효하지 않은 비밀번호 입니다.";

    public static final String CITY_IS_NOT_NULL = "시는 필수 입력 값입니다.";
    public static final String STATE_IS_NOT_NULL = "군/구는 필수 입력 값입니다.";
    public static final String STREET_IS_NOT_NULL = "읍/면/동은 필수 입력 값입니다.";
    public static final String ADDRESS_IS_NOT_NULL = "주소는 필수 입력 값입니다.";

    // 키보드
    public static final String DESCRIPTION_IS_NOT_NULL = "내용은 필수 입력 값입니다.";
    public static final String KEYBOARD_IS_NOT_NULL = "키보드는 필수 입력 값입니다.";

    // 경매
    public static final String TITLE_IS_NOT_NULL = "제목은 필수 입력 값입니다.";
    public static final String START_PRICE_IS_NOT_NULL = "경매 시작가는 필수 입력 값입니다.";
    public static final String START_PRICE_IS_NOT_VALID = "경매 시작가는 최소 1000원이어야 합니다.";
    public static final String BIDDING_UNIT_IS_NOT_NULL = "입찰 단위는 필수 입력 값입니다.";
    public static final String BIDDING_UNIT_IS_NOT_VALID = "입찰 단위는 최소 10원이어야 합니다.";

    public static final String AUCTION_START_DATE_IS_NOT_NULL = "경매 시작일은 필수 입력 값입니다.";
    public static final String AUCTION_START_DATE_IS_NOT_BEFORE_NOW = "경매 시작일은 현재 시각보다 이전일 수 없습니다.";

    public static final String AUCTION_END_DATE_IS_NOT_NULL = "경매 종료일은 필수 입력 값입니다.";
    public static final String AUCTION_END_DATE_IS_NOT_BEFORE_NOW = "경매 종료일은 현재 시각보다 이전일 수 없습니다.";

    // 이메일
    public static final String EMAIL_TITLE_NOT_NULL = "이메일 제목은 필수 입력 값입니다.";
    public static final String EMAIL_CONTENT_NOT_NULL = "이메일 본문은 필수 입력 값입니다.";

    public static final String IMMEDIATE_PURCHASE_PRICE_IS_NOT_VALID = "즉시 구매가는 최소 1000원이어야 합니다.";

}
