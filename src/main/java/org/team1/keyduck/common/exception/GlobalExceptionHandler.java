package org.team1.keyduck.common.exception;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.team1.keyduck.common.dto.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotFoundException(
            DataNotFoundException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataDuplicateException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataDuplicateException(
            DataDuplicateException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataNotMatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotMatchException(
            DataNotMatchException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataUnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataUnauthorizedAccessException(
            DataUnauthorizedAccessException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataNotValidException(
            DataInvalidException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOperationNotAllowedException(
            OperationNotAllowedException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(PaymentConfirmException.class)
    public ResponseEntity<ApiResponse<Void>> handlePaymentConfirmException(
            PaymentConfirmException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getPaymentConfirmErrorCode(), exception.getMessage());
        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(EmailSendErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailSendErrorException(
            EmailSendErrorException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                exception.getErrorCode(), exception.getMessage());
        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    // @RequestBody를 @Valid 해서 에러가 발생한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        RequestBodyValidException customException = new RequestBodyValidException(
                exception.getParameter(), exception.getBindingResult(),
                ErrorCode.INVALID_DATA_VALUE);

        ApiResponse<Void> apiResponse = ApiResponse.error(customException.getErrorCode(),
                customException.getErrorMessage());

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    // @RequestParam를 @Valid 해서 에러가 발생한 경우
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleHandlerMethodValidationExceptionException(
            HandlerMethodValidationException exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(ErrorCode.INVALID_DATA_VALUE,
                Arrays.toString(exception.getDetailMessageArguments()));

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception exception) {
        ApiResponse<Void> apiResponse = ApiResponse.error(
                ErrorCode.INTERNAL_SERVER_ERROR);

        log.info("\n{},{},\n{}", apiResponse.getCode(), getStackTrace(exception.getStackTrace()),
                exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String getStackTrace(StackTraceElement[] stackTraceElements) {
        StringBuilder stackTrace = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            stackTrace.append("\n");
            stackTrace.append(stackTraceElements[i]);
        }

        return stackTrace.toString();
    }
}