package org.team1.keyduck.keyboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public KeyboardCreateResponseDto createKeyboard(KeyboardCreateRequestDto requestDto) {
        // 저장한 유저아이디 가져오기(추후 토큰 적용)

        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

        Keyboard keyboard = Keyboard.builder()
                .member(member)
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .build();

        keyboardRepository.save(keyboard);
        return new KeyboardCreateResponseDto(keyboard.getId(), keyboard.getName(), keyboard.getDescription());
    }

//    public void test() {
//        keyboardRepository.findById(1L).orElseThrow(
//                () -> new DataNotFoundException(ErrorCode.LOGIN2222_FAILED)
//        );
//    }
}