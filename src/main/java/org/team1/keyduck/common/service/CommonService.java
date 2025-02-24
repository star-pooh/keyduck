package org.team1.keyduck.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.exception.DataInvalidException;
import org.team1.keyduck.common.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class CommonService {

    private final PasswordEncoder passwordEncoder;

    public void comparePassword(CharSequence rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new DataInvalidException(ErrorCode.INVALID_DATA_VALUE, "비밀번호");
        }
    }
}
