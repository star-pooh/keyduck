package org.team1.keyduck.common.exception;


import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.team1.keyduck.common.util.ErrorCode;

@Getter
public class RequestBodyValidException extends MethodArgumentNotValidException {

    private final ErrorCode errorCode;
    private final String errorMessage;

    public String getErrorMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getDefaultMessage());
            message.append("\n");
        }
        return message.toString();
    }

    public RequestBodyValidException(MethodParameter parameter,
            BindingResult bindingResult, ErrorCode errorCode) {
        super(parameter, bindingResult);
        this.errorCode = errorCode;
        this.errorMessage = getErrorMessage(bindingResult);
    }


}


