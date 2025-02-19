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
    private String stackTrace;
    private T data;

    private ApiResponse(HttpStatus status, String code, String message, String stackTrace, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.stackTrace = stackTrace;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode, T data) {
        return new ApiResponse<>(successCode.getStatus(), successCode.getCode(),
                successCode.getMessage(), null, data);
    }

    public static <T> ApiResponse<T> success(SuccessCode successCode) {
        return new ApiResponse<>(successCode.getStatus(), successCode.getCode(),
                successCode.getMessage(), null, null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode,
            StackTraceElement[] stackTraceElements) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(),
                errorCode.getMessage(), getStackTrace(stackTraceElements), null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String errorMessage,
            StackTraceElement[] stackTraceElements) {
        return new ApiResponse<>(errorCode.getStatus(), errorCode.getCode(),
                errorMessage, getStackTrace(stackTraceElements), null);
    }

    private static String getStackTrace(StackTraceElement[] stackTraceElements) {
        StringBuilder stackTrace = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            stackTrace.append(stackTraceElements[i]);
            stackTrace.append("\n");
        }

        return stackTrace.toString();
    }

}
