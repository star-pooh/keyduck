package org.team1.keyduck.common.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.common.exception.SuccessCode;

@Getter
public class ApiResponse<T> {

    private HttpStatus status;
    private String code;
    private String message;
    private T data;

    private ApiResponse(HttpStatus status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getStatus(), successCode.getCode(),
                successCode.getMessage(), data);
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getStatus(), successCode.getCode(),
                successCode.getMessage(), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(), errorCode.getMessage(),
                null);
    }
}
