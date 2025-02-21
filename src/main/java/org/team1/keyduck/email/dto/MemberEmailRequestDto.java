package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class MemberEmailRequestDto {

    @NotBlank(message = ValidationErrorMessage.TITLE_IS_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.DESCRIPTION_IS_NOT_NULL)
    private String emailContent;

}
