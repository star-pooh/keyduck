package org.team1.keyduck.keyboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class KeyboardServiceTest {

    // 가짜 객체
    @Mock
    private KeyboardRepository keyboardRepository;
    @Mock
    private MemberRepository memberRepository;

    // 실제 객체 내가 테스트 하려고 하는 진짜 객체
    @InjectMocks
    private KeyboardService keyboardService;


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
        KeyboardCreateRequestDto requestDto = new KeyboardCreateRequestDto(memberId, name, description);
        KeyboardCreateResponseDto result = keyboardService.createKeyboard(requestDto);

        // then
        assertEquals(result.getId(), keyboard.getId());
        assertEquals(result.getName(), name);
        assertEquals(result.getDescription(), description);



    }
}