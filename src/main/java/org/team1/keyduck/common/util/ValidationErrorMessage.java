package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class ValidationErrorMessage {

    public static final String NAME_IS_NOT_NULL = "이름은(는) 필수 입력 값입니다.";
    public static final String EMAIL_IS_NOT_NULL = "이메일은(는) 필수 입력 값입니다.";
    public static final String EMAIL_IS_NOT_VALID = "유효하지 않은 이메일 입니다.";
    public static final String PASSWORD_IS_NOT_AVAILABLE = "유효하지 않은 비밀번호 입니다.";
    public static final String PASSWORD_IS_NOT_NULL = "비밀번호은(는) 필수 입력 값입니다.";
    public static final String MEMBER_ROLE_IS_NOT_NULL = "역할은(는) 필수 입력 값입니다.";
    public static final String ADDRESS_IS_NOT_NULL = "주소은(는) 필수 입력 값입니다.";
    public static final String KEYBOARD_IS_NOT_NULL = "제품은(는) 필수 입력 값입니다.";
    public static final String TITLE_IS_NOT_NULL = "제목은(는) 필수 입력 값입니다.";
    public static final String START_PRICE_IS_NOT_NULL = "경매 시작가는 필수 입력 값입니다.";
    public static final String BIDDING_UNIT_IS_NOT_NULL = "입찰단위는 필수 입력 값입니다.";
    public static final String AUCTION_START_DATE_IS_NOT_NULL = "경매시작일은 필수 입력 값입니다.";
    public static final String AUCTION_END_DATE_IS_NOT_NULL = "경매시작일은 필수 입력 값입니다.";
    public static final String AUCTION_START_DATE_IS_NOT_BEFORE_NOW = "경매 시작일은 현재 시각보다 이전일 수 없습니다.";
    public static final String AUCTION_END_DATE_IS_NOT_BEFORE_NOW = "경매 종료일은 현재 시각보다 이전일 수 없습니다.";

}