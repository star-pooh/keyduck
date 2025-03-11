package org.team1.keyduck.common.dto;

import lombok.Getter;
import org.team1.keyduck.common.exception.ErrorCode;


@Getter
public class ErrorResponse {

    private final String errorCode;
    private final String errorMessage;
    private final String detail;

    private ErrorResponse(ErrorCode errorCode, String detail) {
        this.errorCode = errorCode.getCode();
        this.errorMessage = errorCode.getMessage();
        this.detail = detail;
    }

    public static ErrorResponse of(ErrorCode errorCode, String detail) {
        return new ErrorResponse(errorCode, detail);
    }

}
