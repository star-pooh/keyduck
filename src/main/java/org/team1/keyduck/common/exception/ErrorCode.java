package org.team1.keyduck.common.exception;


import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.team1.keyduck.common.util.ErrorMessage;

@Getter
public enum ErrorCode {
    // 400 BAD_REQUEST
    // INVALID(유효하지 않은 값)
    INVALID_DATA_VALUE(HttpStatus.BAD_REQUEST, "INVALID_001", ErrorMessage.INVALID_VALUE),
    INVALID_DATA_TYPE(HttpStatus.BAD_REQUEST, "INVALID_002", ErrorMessage.INVALID_VALUE),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "INVALID_003", ErrorMessage.INVALID_VALUE),
    INVALID_ACCESS(HttpStatus.BAD_REQUEST, "INVALID_004", ErrorMessage.INVALID_VALUE),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "INVALID_005", ErrorMessage.INVALID_VALUE),
    INVALID_BIDDING_PRICE_UNIT(HttpStatus.BAD_REQUEST, "INVALID_006",
            ErrorMessage.INVALID_BIDDING_PRICE_UNIT),
    AUCTION_NOT_IN_PROGRESS(HttpStatus.BAD_REQUEST, "INVALID_007",
            ErrorMessage.NOT_IN_PROGRESS_AUCTION),
    MAX_BIDDING_COUNT_EXCEEDED(HttpStatus.BAD_REQUEST, "INVALID_008",
            ErrorMessage.EXCEED_BIDDING_COUNT),
    BIDDING_PRICE_BELOW_CURRENT_PRICE(HttpStatus.BAD_REQUEST, "INVALID_009",
            ErrorMessage.LOWER_BIDDING_PRICE),
    BIDDING_PRICE_EXCEEDS_MAX_LIMIT(HttpStatus.BAD_REQUEST, "INVALID_010",
            ErrorMessage.EXCEED_MAX_BIDDING_PRICE),

    // DUPLICATE(중복되는 값)
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DUPLICATE_001", ErrorMessage.DUPLICATE_USE),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "DUPLICATE_002", ErrorMessage.DUPLICATE_USE),
    DUPLICATE_DELETED(HttpStatus.BAD_REQUEST, "DUPLICATE_003", ErrorMessage.DUPLICATE_DELETE),


    // 401 UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "UNAUTH_001", ErrorMessage.UNAUTHORIZED_ACCESS),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "UNAUTH_002", ErrorMessage.LOGIN_FAILED),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "UNAUTH_003", ErrorMessage.INVALID_VALUE),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "FORBIDDEN_001", ErrorMessage.FORBIDDEN_ACCESS),

    // 404 NOT_FOUND
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "NOTFOUND_001", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, "NOTFOUND_002", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_KEYBOARD(HttpStatus.NOT_FOUND, "NOTFOUND_003", ErrorMessage.NOT_FOUND_VALUE),
    NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "NOTFOUND_004", ErrorMessage.NOT_FOUND_VALUE),

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

