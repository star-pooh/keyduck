package org.team1.keyduck.keyboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
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

        return KeyboardCreateResponseDto.of(keyboard);
    }

    @Transactional(readOnly = true)
    public List<KeyboardReadResponseDto> findKeyboardBySellerIdService(Long sellerId) {

        List<Keyboard> keyboards = keyboardRepository.findAllByMemberId(sellerId);

        return keyboards.stream()
                .map(KeyboardReadResponseDto::of)
                .toList();
    }

}
