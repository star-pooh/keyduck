package org.team1.keyduck.common.exception;


import lombok.Getter;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
public class CustomValidException extends MethodArgumentNotValidException {

    private final ErrorCode errorCode;
    private final String errorMessage;

    public String getMessage(BindingResult bindingResult) {
        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append(fieldError.getDefaultMessage());
            message.append("\n");
        }
        return message.toString();
    }

    public CustomValidException(MethodParameter parameter,
        BindingResult bindingResult, ErrorCode errorCode) {
        super(parameter, bindingResult);
        this.errorCode = errorCode;
        errorMessage = getMessage(bindingResult);
    }

}


