package org.team1.keyduck.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class MemberUpdatePasswordRequestDto {

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    private String beforePassword;

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    @Pattern(regexp = Constants.PASSWORD_REGEXP, message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    private String modifyPassword;

}
