package org.team1.keyduck.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.team1.keyduck.common.util.ValidationErrorMessage;
import org.team1.keyduck.member.entity.MemberRole;

//MethodArgumentNotValidException

@Getter
public class MemberCreateRequestDto {

    @NotNull(message = ValidationErrorMessage.NAME_IS_NOT_NULL)
    String name;

    @Email(message = ValidationErrorMessage.EMAIL_IS_NOT_VALID)
    String email;

    @NotNull(message = ValidationErrorMessage.PASSWORD_IS_NOT_NULL)
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
//        message = ValidationErrorMessage.PASSWORD_IS_NOT_AVAILABLE)
    String password;
    //TODO 비밀번호 정규식, 배포시 수정할 것

    @NotNull(message = ValidationErrorMessage.MEMBER_ROLE_IS_NOT_NULL)
    MemberRole memberRole;
}
