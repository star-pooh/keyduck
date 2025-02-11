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


    //401  UNAUTHORIZED
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "ERR101", "인증이 필요한 접근입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "ERR102", "로그인에 실패했습니다."),
    LOGIN2222_FAILED(HttpStatus.UNAUTHORIZED, "ERR103", "로그인에 실패했습니다."),

    // 403 FORBIDDEN
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "ERR201", "접근 권한이 없습니다."),

    // 404 NOT_FOUND
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR301", "사용자를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "ERR300", "요청한 리소스를 찾을 수 없습니다."),

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

