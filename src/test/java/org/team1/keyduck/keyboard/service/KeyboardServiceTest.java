package org.team1.keyduck.keyboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD2;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD3;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD_ID2;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD_ID3;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;

@ExtendWith(MockitoExtension.class)
@Transactional
class KeyboardServiceTest {

    @Mock
    private KeyboardRepository keyboardRepository;

    @InjectMocks
    private KeyboardService keyboardService;

    @Test
    @DisplayName("키보드 조회_성공_키보드가 1개일때")
    public void findKeyboard_success_one() {
        //given : 설정(테스트준비)
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);
        ReflectionTestUtils.setField(keyboard, "id", TEST_KEYBOARD_ID1);
        //when : 수행할 작업(테스트 검증을 위한 준비)
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(List.of(keyboard));
        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(keyboard.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("키보드 조회_성공_키보드가 여러개일때")
    public void findKeyboard_success_many() {

        //given : 설정
        Member member = TEST_MEMBER1;
        Keyboard keyboard = TEST_KEYBOARD1;
        Keyboard keyboard2 = TEST_KEYBOARD2;
        Keyboard keyboard3 = TEST_KEYBOARD3;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);
        ReflectionTestUtils.setField(keyboard, "id", TEST_KEYBOARD_ID1);
        ReflectionTestUtils.setField(keyboard2, "id", TEST_KEYBOARD_ID2);
        ReflectionTestUtils.setField(keyboard3, "id", TEST_KEYBOARD_ID3);
        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(
                List.of(keyboard, keyboard2, keyboard3));

        List<Keyboard> result = keyboardRepository.findAllByMemberId(member.getId());

        //then : 결과검증
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("키보드 조회_키보드가 없을때")
    public void findKeyboard_empty() {

        //given : 설정
        Member member = TEST_MEMBER1;

        ReflectionTestUtils.setField(member, "id", TEST_ID1);

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(
                Collections.emptyList());

        //then : 결과검증
        List<Keyboard> result = keyboardRepository.findAllByMemberId(TEST_ID1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}