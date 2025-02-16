package org.team1.keyduck.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //400 BAD_REQUEST
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "ERR001", "요청값이 올바르지 않습니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "ERR002", "요청 데이터 타입이 올바르지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "ERR003", "이미 사용 중인 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "ERR004", "이미 사용 중인 사용자명입니다."),
    WRONG_CREDENTIALS(HttpStatus.BAD_REQUEST, "ERR005", "인증 정보가 올바르지 않습니다."),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "ERR006", "잘못된 접근입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "ERR007", "잘못된 JSON 형식입니다."),
    INVALID_MEMBER_ROLE(HttpStatus.BAD_REQUEST, "ERR008", "잘못된 권한입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "ERR009", "잘못된 토큰입니다."),
    DUPLICATE_DELETED(HttpStatus.BAD_REQUEST, "ERR010", "이미 삭제된 키보드입니다."),
    AUCTION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "ERR011", "진행중인 경매가 아닙니다."),
    MAX_BIDDING_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "ERR012", "입찰은 10번까지만 가능합니다."),
    INVALID_BIDDING_PRICE_UNIT(HttpStatus.BAD_REQUEST, "ERR013", "입찰 금액이 단위에 맞지 않습니다."),
    BIDDING_PRICE_BELOW_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "ERR014", "입찰 금액이 현재가보다 작습니다."),
    BIDDING_PRICE_EXCEEDS_MAX_LIMIT(HttpStatus.BAD_REQUEST, "ERR015", "입찰 금액이 최대 가능 금액보다 큽니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "ERR016", "결제 금액이 올바르지 않습니다."),
    INSUFFICIENT_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "ERR016", "결제 금액이 부족합니다."),

    //401  UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "ERR101", "인증이 필요한 접근입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "ERR102", "로그인에 실패했습니다."),
    LOGIN2222_FAILED(HttpStatus.UNAUTHORIZED, "ERR103", "로그인에 실패했습니다."),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "ERR201", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR301", "사용자를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR300", "요청한 리소스를 찾을 수 없습니다."),
    KEYBOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR303", "해당 아이디를 가진 키보드를 찾을 수 없습니다."),
    AUCTION_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR304", "해당 경매를 찾을 수 없습니다."),
    PAYMENT_METHOD_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR305", "결제 수단에 대한 정보를 찾을 수 없습니다."),
    TEMP_PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR306", "결제 금액에 대한 정보를 찾을 수 없습니다."),
    PAYMENT_DEPOSIT_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR307", "예치금에 대한 정보를 찾을 수 없습니다."),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "ERR999", "서버 내부 오류가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}

