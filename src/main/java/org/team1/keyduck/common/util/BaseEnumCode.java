package org.team1.keyduck.common.util;

import org.springframework.http.HttpStatus;

public interface BaseEnumCode {

    HttpStatus getStatus();

    String getCode();

    String getMessage();
}
