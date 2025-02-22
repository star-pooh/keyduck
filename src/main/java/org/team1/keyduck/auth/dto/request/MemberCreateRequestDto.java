package org.team1.keyduck.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.Constants;
import org.team1.keyduck.common.util.ValidationErrorMessage;
import org.team1.keyduck.member.entity.Address;


@Getter
public class MemberCreateRequestDto {

    @NotBlank(message = ValidationErrorMessage.NAME_IS_NOT_NULL)
    String name;

    @NotBlank(message = ValidationErrorMessage.EMAIL_IS_NOT_NULL)
    @Pattern(regexp = Constants.EMAIL_REGEXP, message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    String email;

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    @Pattern(regexp = Constants.PASSWORD_REGEXP, message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    String password;

    @NotNull(message = ValidationErrorMessage.ADDRESS_IS_NOT_NULL)
    Address address;
}
