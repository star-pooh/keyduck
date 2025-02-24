package org.team1.keyduck.common.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD3;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.team1.keyduck.common.exception.DataInvalidException;

@ExtendWith(MockitoExtension.class)
class CommonServiceTest {

    @InjectMocks
    CommonService commonService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 비밀번호_비교_실패() {
        // given
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(false);

        // when&then
        assertThrows(DataInvalidException.class, () -> {
            commonService.comparePassword(TEST_PASSWORD1, TEST_PASSWORD3);
        });
    }
}