package org.team1.keyduck.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;
import org.team1.keyduck.member.entity.Address;
import org.team1.keyduck.member.entity.MemberRole;


@Getter
public class MemberCreateRequestDto {

    @NotBlank(message = ValidationErrorMessage.NAME_IS_NOT_NULL)
    String name;

    @NotBlank(message = ValidationErrorMessage.EMAIL_IS_NOT_NULL)
    @Email(message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    String email;

    @NotBlank(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    String password;

    @NotNull(message = ValidationErrorMessage.MEMBER_ROLE_IS_NOT_NULL)
    MemberRole memberRole;

    Address address;
}
