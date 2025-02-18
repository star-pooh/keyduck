package org.team1.keyduck.keyboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class KeyboardCreateRequestDto {

    @NotBlank(message = ValidationErrorMessage.NAME_IS_NOT_NULL)
    private String name;

    @NotBlank(message = ValidationErrorMessage.DESCRIPTION_IS_NOT_NULL)
    private String description;


}
