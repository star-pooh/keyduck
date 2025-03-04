package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class MemberEmailRequestDto {

    @NotBlank(message = ValidationErrorMessage.EMAIL_TITLE_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.EMAIL_CONTENT_NOT_NULL)
    private String emailContent;

}
