package org.team1.keyduck.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // 200 OK
    READ_SUCCESS(HttpStatus.OK, "OK_001", "정상적으로 조회되었습니다."),
    UPDATE_SUCCESS(HttpStatus.OK, "OK_002", "정상적으로 수정되었습니다."),
    DELETE_SUCCESS(HttpStatus.OK, "OK_003", "정상적으로 삭제되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "OK_004", "로그인 되었습니다."),
    TOKEN_VERIFY_SUCCESS(HttpStatus.OK, "OK_005", "토큰이 검증 되었습니다."),

    // 201 CREATED
    CREATE_SUCCESS(HttpStatus.CREATED, "CRATED_001", "정상적으로 생성되었습니다."),

    SEND_SUCCESS(HttpStatus.CREATED, "SEND_001", "정상적으로 전송되었습니다");

    private final HttpStatus status;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
