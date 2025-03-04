package org.team1.keyduck.common.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD1;
import static org.team1.keyduck.testdata.TestData.TEST_PASSWORD3;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName(value = "비밀번호 비교시 일치하지 않음")
    void passwordMatchesInconsistency() {
        // given
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenReturn(false);

        // when&then
        DataInvalidException exception = assertThrows(DataInvalidException.class, () -> {
            commonService.comparePassword(TEST_PASSWORD1, TEST_PASSWORD3);
        });

        assertEquals("유효하지 않은 비밀번호 입니다.", exception.getMessage());
    }
}