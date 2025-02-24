package org.team1.keyduck.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {

    @NotBlank(message = ValidationErrorMessage.EMAIL_IS_NOT_NULL)
    @Pattern(regexp = Constants.EMAIL_REGEXP, message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    private String email;

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    @Pattern(regexp = Constants.PASSWORD_REGEXP,
            message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    private String password;
}
