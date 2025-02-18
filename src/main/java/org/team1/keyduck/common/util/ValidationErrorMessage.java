package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class ValidationErrorMessage {

    public static final String NAME_IS_NOT_NULL = "이름은(는) 필수 입력 값입니다.";
    public static final String EMAIL_IS_NOT_NULL = "이메일은(는) 필수 입력 값입니다.";
    public static final String EMAIL_IS_NOT_VALID = "유효하지 않은 이메일 입니다.";
    public static final String PASSWORD_IS_NOT_AVAILABLE = "유효하지 않은 비밀번호 입니다.";
    public static final String PASSWORD_IS_NOT_NULL = "비밀번호은(는) 필수 입력 값입니다.";
    public static final String MEMBER_ROLE_IS_NOT_NULL = "역할은(는) 필수 입력 값입니다.";
    public static final String ADDRESS_IS_NOT_NULL = "주소은(는) 필수 입력 값입니다.";
    

}