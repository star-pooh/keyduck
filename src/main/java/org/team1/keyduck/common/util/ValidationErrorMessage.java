package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class ValidationErrorMessage {

    public static final String NAME_IS_NOT_NULL = "이름은 비울 수 없습니다.";
    public static final String EMAIL_IS_NOT_NULL = "이메일은 비울 수 없습니다.";
    public static final String EMAIL_IS_NOT_VALID = "이메일 형식이 맞지 않습니다.";
    public static final String PASSWORD_IS_NOT_AVAILABLE = "비밀번호 형식이 맞지 않습니다.";
    public static final String PASSWORD_IS_NOT_NULL = "비밀번호는 비울 수 없습니다.";
    public static final String MEMBER_ROLE_IS_NOT_NULL = "역할은 필수로 지정해야합니다.";

}