package org.team1.keyduck.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.team1.keyduck.common.util.BaseEnumCode;
import org.team1.keyduck.common.util.ErrorMessage;

@Getter
public enum ErrorCode implements BaseEnumCode {
    // 400 BAD_REQUEST
    // INVALID(유효하지 않은 값)
    INVALID_DATA_VALUE(HttpStatus.BAD_REQUEST, "BAD_INVALID_001", ErrorMessage.INVALID_VALUE),
    INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST, "BAD_INVALID_002", ErrorMessage.INVALID_VALUE),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "BAD_INVALID_003", ErrorMessage.INVALID_VALUE),
    INVALID_BIDDING_PRICE_UNIT(HttpStatus.BAD_REQUEST, "BAD_INVALID_004",
            ErrorMessage.INVALID_BIDDING_PRICE_UNIT),
    AUCTION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "BAD_INVALID_005",
            ErrorMessage.NOT_IN_PROGRESS_AUCTION),
    MAX_BIDDING_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "BAD_INVALID_006",
            ErrorMessage.EXCEED_BIDDING_COUNT),
    BIDDING_PRICE_BELOW_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "BAD_INVALID_007",
            ErrorMessage.LOWER_BIDDING_PRICE),
    BIDDING_PRICE_EXCEEDS_MAX_LIMIT(HttpStatus.BAD_REQUEST, "BAD_INVALID_008",
            ErrorMessage.EXCEED_MAX_BIDDING_PRICE),
    INSUFFICIENT_PAYMENT_DEPOSIT_AMOUNT(HttpStatus.BAD_REQUEST, "BAD_INVALID_009",
            ErrorMessage.INSUFFICIENT_PAYMENT_DEPOSIT_AMOUNT),
    AUCTION_NOT_MODIFIABLE_AND_DELETABLE(HttpStatus.BAD_REQUEST, "BAD_INVALID_010",
            ErrorMessage.NOT_MODIFIABLE_AND_DELETABLE),
    DELETE_FAIL_AUCTION_IN_PROGRESS(HttpStatus.BAD_REQUEST, "BAD_INVALID_011",
            ErrorMessage.DELETE_FAIL_AUCTION_IN_PROGRESS),
    BEFORE_INFO_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "BAD_INVALID_012",
            ErrorMessage.BEFORE_INFO_NOT_AVAILABLE),
    EMPTY_REQUEST_BODY(HttpStatus.BAD_REQUEST, "INVALID_013", ErrorMessage.EMPTY_REQUEST_BODY),
    NOT_MODIFIABLE_DELETED_KEYBOARD(HttpStatus.BAD_REQUEST, "BAD_INVALID_014",
            ErrorMessage.NOT_MODIFIABLE_DELETED_KEYBOARD),
    NOT_DELETABLE_KEYBOARD_DUE_TO_AUCTION(HttpStatus.BAD_REQUEST, "BAD_INVALID_015",
            ErrorMessage.NOT_DELETABLE_KEYBOARD_DUE_TO_AUCTION),

    // DUPLICATE(중복되는 값)
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "BAD_DUPLICATE_001", ErrorMessage.DUPLICATE_USE),
    DUPLICATE_DELETED(HttpStatus.BAD_REQUEST, "BAD_DUPLICATE_002", ErrorMessage.DUPLICATE_DELETE),
    DUPLICATE_KEYBOARD(HttpStatus.BAD_REQUEST, "BAD_DUPLICATE_003",
            ErrorMessage.DUPLICATE_KEYBOARD),


    // 401 UNAUTHORIZED
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "UNAUTH_001", ErrorMessage.LOGIN_FAILED),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTH_002", ErrorMessage.INVALID_VALUE),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "FORBIDDEN_001", ErrorMessage.FORBIDDEN_ACCESS),
    FORBIDDEN_PAYMENT_FROM_SELLER(HttpStatus.FORBIDDEN, "FORBIDDEN_002",
            ErrorMessage.FORBIDDEN_PAYMENT_FROM_SELLER),

    // 404 NOT_FOUND
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NOTFOUND_001", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_KEYBOARD(HttpStatus.NOT_FOUND, "NOTFOUND_002", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "NOTFOUND_003", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_PAYMENT_METHOD(HttpStatus.NOT_FOUND, "NOTFOUND_004", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_TEMP_PAYMENT(HttpStatus.NOT_FOUND, "NOTFOUND_005", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "NOTFOUND_006", ErrorMessage.NOT_FOUND_VALUE),

    // 500 INTERNAL_SERVER_ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_001",
            ErrorMessage.INTERNAL_SERVER_ERROR);


    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String getMessage(Object arg) {
        return arg != null ? String.format(this.message, arg) : this.message;
    }
}

