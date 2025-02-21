package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class GeneralEmailRequestDto {

    @NotBlank(message = ValidationErrorMessage.EMAIL_IS_NOT_NULL)
    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    private String recipientEmail;

    @NotBlank(message = ValidationErrorMessage.TITLE_IS_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.DESCRIPTION_IS_NOT_NULL)
    private String emailContent;
}
