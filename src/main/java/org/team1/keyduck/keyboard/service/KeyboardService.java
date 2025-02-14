package org.team1.keyduck.keyboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;
import org.team1.keyduck.member.entity.Member;
import org.team1.keyduck.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;
    private final MemberRepository memberRepository;

    // 키보드 생성
    @Transactional
    public KeyboardCreateResponseDto createKeyboard(Long memberId, KeyboardCreateRequestDto requestDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.USER_NOT_FOUND));

        Keyboard keyboard = Keyboard.builder()
                .member(member)
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .build();

        keyboardRepository.save(keyboard);
        return KeyboardCreateResponseDto.of(keyboard);
    }

}
