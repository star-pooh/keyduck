package org.team1.keyduck.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.team1.keyduck.member.entity.MemberRole;

@Getter
public class MemberCreateRequestDto {

    @NotNull(message = "이름은 비울 수 없습니다.")
    String name;

    @Email(message = "이메일 형식이 맞지 않습니다.")
    String email;

    @NotNull(message = "비밀번호는 필수 입니다.")
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    String password;
    //TODO 비밀번호 정규식, 배포시 수정할 것

    @NotNull(message = "역할은 필수로 지정해야합니다.")
    MemberRole memberRole;
}
