package org.team1.keyduck.keyboard.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.team1.keyduck.testdata.TestData.TEST_ID1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD1;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD2;
import static org.team1.keyduck.testdata.TestData.TEST_KEYBOARD3;
import static org.team1.keyduck.testdata.TestData.TEST_MEMBER1;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.testdata.TestData;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
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

        //when : 수행할 작업(테스트 검증을 위한 준비)
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(List.of(keyboard));
        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        assertEquals(1, result.size());
        assertEquals(keyboard.getId(), result.get(0).getId());
    }

    @Test
    @DisplayName("키보드 조회_성공_키보드가 여러개일때")
    public void findKeyboard_success_many() {

        //given : 설정
        Member member = TEST_MEMBER1;

        List<Keyboard> keyboardList = List.of(TestData.TEST_KEYBOARD1, TEST_KEYBOARD2,
                TEST_KEYBOARD3);

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(keyboardList);

        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(
                member.getId());

        //then : 결과검증
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("키보드 조회_키보드가 없을때")
    public void findKeyboard_empty() {

        //given : 설정
        Member member = TEST_MEMBER1;

        //when : 수행할 작업
        when(keyboardRepository.findAllByMemberId(member.getId())).thenReturn(
                Collections.emptyList());

        //then : 결과검증
        List<KeyboardReadResponseDto> result = keyboardService.findKeyboardBySellerId(TEST_ID1);

        assertTrue(result.isEmpty());
    }

    // 가짜 객체
    @Mock
    private MemberRepository memberRepository;

    @Test
    @DisplayName("키보드 생성 메서드 - 성공 케이스")
    public void createKeyboard_success() {
        // give
        Long memberId = 1L;
        String name = "키보드";
        String description = "키보드입니다.";

        Member member = mock(Member.class);
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Keyboard 객체 생성하고
        Keyboard keyboard = Keyboard.builder()
                .member(member)
                .name(name)
                .description(description)
                .build();
        // 키보드 아이디 생성자가 없으니까 ReflectionTestUtils로 키보드 객체 수정
        ReflectionTestUtils.setField(keyboard, "id", 1L);

        // any : 어떤값이든 상관없이 그 타입이면 okay
        when(keyboardRepository.save(any(Keyboard.class))).thenReturn(keyboard);

        // when
        KeyboardCreateRequestDto requestDto = new KeyboardCreateRequestDto(name, description);
        KeyboardCreateResponseDto result = keyboardService.createKeyboard(memberId, requestDto);

        // then
        //assertEquals(result.getId(), keyboard.getId());
        assertEquals(result.getName(), name);
        assertEquals(result.getDescription(), description);

    }

    @Test
    @DisplayName("키보드 생성 메서드(실패 케이스) - 유저가 존재하지 않는 경우")
    void createKeyboard_fail() {

        // give
        Long memberId = 1L;
        String name = "키보드";
        String description = "키보드입니다.";

        when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

        KeyboardCreateRequestDto requestDto = new KeyboardCreateRequestDto(name, description);
        // when

        // then
        Assertions.assertThrows(DataNotFoundException.class, () -> {
            KeyboardCreateResponseDto result = keyboardService.createKeyboard(memberId, requestDto);
        });

    }
}