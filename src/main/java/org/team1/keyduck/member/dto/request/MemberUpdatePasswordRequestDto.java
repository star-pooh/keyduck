package org.team1.keyduck.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
public class MemberUpdatePasswordRequestDto {

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    String beforePassword;

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    String modifyPassword;

}
