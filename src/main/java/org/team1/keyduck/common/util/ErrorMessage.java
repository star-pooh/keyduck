package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class ErrorMessage {

    // 중복되는 메시지
    public static final String INVALID_VALUE = "유효하지 않은 %s 입니다.";
    public static final String DUPLICATE_USE = "해당 %s은(는) 이미 사용 중입니다.";
    public static final String DUPLICATE_DELETE = "이미 삭제된 %s 입니다.";
    public static final String NOT_FOUND_VALUE = "해당 %s을(를) 찾을 수 없습니다.";

    // 중복되지 않는 메시지
    // 400 BAD_REQUEST
    public static final String INVALID_BIDDING_PRICE_UNIT = "최소 입찰 금액 단위의 배수가 아닙니다.";
    public static final String NOT_IN_PROGRESS_AUCTION = "진행 중인 경매가 아닙니다.";
    public static final String EXCEED_BIDDING_COUNT = "입찰은 10번까지만 가능합니다.";
    public static final String LOWER_BIDDING_PRICE = "입찰가가 현재가보다 작습니다.";
    public static final String EXCEED_MAX_BIDDING_PRICE = "입찰가가 1회 입찰 시 가능한 최대 금액(최소 입찰 금액 단위의 10배)을 초과하였습니다.";
    public static final String INSUFFICIENT_PAYMENT_DEPOSIT_AMOUNT = "입찰 가능한 예치금이 부족합니다.";
    public static final String REQUIRED_TOKEN = "JWT 토큰이 필요합니다.";
    public static final String INVALID_TOKEN = "잘못된 JWT 토큰 입니다.";
    public static final String UNSUPPORTED_TOKEN = "지원되지 않는 JWT 토큰 입니다.";
    public static final String NOT_MODIFIABLE_AND_DELETEABLE = "진행 중이거나 종료된 경매는 수정 및 삭제할 수 없습니다.";
    public static final String BEFORE_INFO_NOT_AVAILABLE = "이전 정보와 같은 %s로 변경할 수 없습니다.";


    // 401 UNAUTHORIZED
    public static final String UNAUTHORIZED_ACCESS = "인증이 필요한 접근입니다.";
    public static final String LOGIN_FAILED = "로그인에 실패했습니다.";
    public static final String INVALID_TOKEN_SIGNATURE = "유효하지 않은 JWT 서명입니다.";
    public static final String EXPIRED_TOKEN = "만료된 JWT 토큰입니다.";

    // 403 FORBIDDEN
    public static final String FORBIDDEN_ACCESS = "접근 권한이 없습니다.";
    public static final String FORBIDDEN_PAYMENT_LOGIN_FORM = "판매자는 경매 포인트를 충전 할 수 없습니다.";
    public static final String NOT_AVAILABLE_TOKEN_DELETE_MEMBER = "삭제된 멤버의 JWT 토큰은 사용할 수 없습니다.";
    public static final String DELETE_FAIL_AUCTION_IN_PROGRESS = "진행 중인 경매가 있기 때문에 삭제가 불가능 합니다.";

    // 500 INTERNAL_SERVER_ERROR
    public static final String INTERNAL_SERVER_ERROR = "서버 내부 오류가 발생했습니다.";
}
