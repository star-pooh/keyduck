package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEmailRequestDto {

    @NotBlank(message = ValidationErrorMessage.TITLE_IS_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.DESCRIPTION_IS_NOT_NULL)
    private String emailContent;


}
