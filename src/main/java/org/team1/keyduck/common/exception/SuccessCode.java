package org.team1.keyduck.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {

    // 200 OK
    READ_SUCCESS(HttpStatus.OK, "SUCC001", "정상적으로 조회되었습니다."),
    UPDATE_SUCCESS(HttpStatus.OK, "SUCC002", "정상적으로 수정되었습니다."),
    DELETE_SUCCESS(HttpStatus.OK, "SUCC003", "정상적으로 삭제되었습니다."),
    LOGIN_SUCCESS(HttpStatus.OK, "SUCC004", "로그인 되었습니다."),

    // 201 CREATED
    CREATE_SUCCESS(HttpStatus.CREATED, "SUCC101", "정상적으로 생성되었습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
