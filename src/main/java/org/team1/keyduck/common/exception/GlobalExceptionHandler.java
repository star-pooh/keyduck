package org.team1.keyduck.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.team1.keyduck.common.dto.ApiResponse;
import org.team1.keyduck.common.util.ErrorMessage;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDataNotFoundException(
            DataNotFoundException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataDuplicateException.class)
    public ResponseEntity<ApiResponse> handleDataDuplicateException(
            DataDuplicateException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataNotMatchException.class)
    public ResponseEntity<ApiResponse> handleDataNotMatchException(
            DataNotMatchException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataUnauthorizedAccessException.class)
    public ResponseEntity<ApiResponse> handleDataUnauthorizedAccessException(
            DataUnauthorizedAccessException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(DataInvalidException.class)
    public ResponseEntity<ApiResponse> handleDataNotValidException(
            DataInvalidException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ApiResponse> handleOperationNotAllowedException(
            OperationNotAllowedException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }


    // @RequestBody를 @Valid 해서 에러가 발생한 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        RequestBodyValidException customException = new RequestBodyValidException(
                exception.getParameter(), exception.getBindingResult(),
                ErrorCode.INVALID_DATA_VALUE);
        ApiResponse apiResponse = ApiResponse.error(customException.getErrorCode(),
                customException.getErrorMessage());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }

    // @RequestParam를 @Valid 해서 에러가 발생한 경우
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse> handleHandlerMethodValidationExceptionException(
            HandlerMethodValidationException exception) {
        String fieldName = "";
        Object[] arguments = exception.getDetailMessageArguments();
        if (arguments != null && arguments.length > 0
                && arguments[0] instanceof String extractedField) {
            if ("price".equals(extractedField)) {
                fieldName = "입찰가격";
            }
        }
        String errorMessage = String.format(ErrorMessage.REQUIRED_VALUE, fieldName);
        ApiResponse apiResponse = ApiResponse.error(ErrorCode.REQUIRED_VALUE, errorMessage);
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        ApiResponse apiResponse = ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
                exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}