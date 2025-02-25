package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class GeneralEmailRequestDto {

    @NotBlank(message = ValidationErrorMessage.EMAIL_IS_NOT_NULL)
    @Pattern(regexp = Constants.EMAIL_REGEXP, message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    private String recipientEmail;

    @NotBlank(message = ValidationErrorMessage.EMAIL_TITLE_IS_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.EMAIL_CONTENT_IS_NOT_NULL)
    private String emailContent;
}
