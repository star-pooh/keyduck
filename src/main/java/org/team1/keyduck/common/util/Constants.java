package org.team1.keyduck.common.util;

import java.util.List;
import lombok.Getter;

@Getter
public class Constants {

    public static final int SUCCESS_BIDDING_PAGE_SIZE = 10;

    public static final Long MAX_BIDDING_MULTIPLE = 10L;

    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";

    public static final List<String> WHITE_LIST = List.of(
            "/api/payment/process",
            "/api/payment/success",
            "/api/auth",
            "/payment_login",
            "/token_verify",
            "/style"
    );

    public static final String AUCTION_CREATED_MAIL_TITLE = "경매가 성공적으로 생성되었습니다.";

    public static final String AUCTION_CREATED_MAIL_CONTENTS = "%님의 키보드 %의 경매 %가 정상적으로 등록되었습니다.";
}
