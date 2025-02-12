package org.team1.keyduck.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.team1.keyduck.common.dto.ApiResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDataNotFoundException(DataNotFoundException exception) {
        ApiResponse apiResponse = ApiResponse.error(exception.getErrorCode());
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
            apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DuplicateDataException.class)
    public ResponseEntity<ApiResponse> handleDuplicateDataException(DuplicateDataException exception) {
        ApiResponse apiResponse = ApiResponse.error(ErrorCode.DUPLICATE_EMAIL);
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
            apiResponse.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {
        ApiResponse apiResponse = ApiResponse.error(ErrorCode.INTERNAL_SERVER_ERROR);
        log.info("{}, {}, {}", apiResponse.getCode(), exception.getStackTrace(),
            exception.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
