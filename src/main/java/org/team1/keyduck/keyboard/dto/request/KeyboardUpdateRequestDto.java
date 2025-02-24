package org.team1.keyduck.keyboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
@AllArgsConstructor
public class KeyboardUpdateRequestDto {

    @NotBlank(message = ValidationErrorMessage.NAME_IS_NOT_NULL)
    private String name;

    @NotBlank(message = ValidationErrorMessage.DESCRIPTION_IS_NOT_NULL)
    private String description;

}
