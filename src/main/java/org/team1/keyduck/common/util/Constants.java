package org.team1.keyduck.common.util;

import java.util.List;
import lombok.Getter;

@Getter
public class Constants {

    public static final int SUCCESS_BIDDING_PAGE_SIZE = 10;

    public static final Long MAX_BIDDING_MULTIPLE = 10L;

    public static final int SCHEDULER_THREAD_POOL_SIZE = 2;

    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";

    public static final List<String> WHITE_LIST = List.of(
            "/style",
            "/token_verify",
            "/login",
            "/main",
            "/payment_",
            "/api/auth",
            "/api/auctions/main",
            "/auction_detail",
            "/ws/auction"
    );

    public static final String AUCTION_CREATED_MAIL_TITLE = "경매가 성공적으로 생성되었습니다.";

    public static final String AUCTION_CREATED_MAIL_CONTENTS = "%s님의 키보드 %s의 경매 %s가 정상적으로 등록되었습니다.";

    public static final int PAYMENT_CONFIRM_MAX_RETRIES = 3;

    public static final String PAYMENT_COMPLETION_EMAIL_TITLE = "키덕키덕 충전 완료 - 입찰을 시작해보세요!";

    public static final String PAYMENT_COMPLETION_EMAIL_CONTENTS = "충전금액 %s, 결제 수단 %s 로 충전이 완료 되었습니다. 원하는 경매에 참여해보세요.";
}
