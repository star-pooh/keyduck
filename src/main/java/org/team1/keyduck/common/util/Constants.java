package org.team1.keyduck.common.util;

import lombok.Getter;

@Getter
public class Constants {

    public static final int SUCCESS_BIDDING_PAGE_SIZE = 10;

    public static final Long MAX_BIDDING_MULTIPLE = 10L;

    public static final String EMAIL_REGEXP = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static final String PASSWORD_REGEXP = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}";
}
