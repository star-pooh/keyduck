package org.team1.keyduck.keyboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataDuplicateException;
import org.team1.keyduck.common.exception.DataMismatchException;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.DataUnauthorizedAccessException;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.dto.request.KeyboardCreateRequestDto;
import org.team1.keyduck.keyboard.dto.request.KeyboardUpdateRequestDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardCreateResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.dto.response.KeyboardUpdateResponseDto;
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
    public KeyboardCreateResponseDto createKeyboard(Long memberId,
            KeyboardCreateRequestDto requestDto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_USER, "회원"));

        Keyboard keyboard = Keyboard.builder()
                .member(member)
                .name(requestDto.getName())
                .description(requestDto.getDescription())
                .build();

        keyboardRepository.save(keyboard);
        return KeyboardCreateResponseDto.of(keyboard);
    }

    // 키보드 삭제
    @Transactional
    public void deleteKeyboard(Long keyboardId, Long memberId) {

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_KEYBOARD, "키보드"));

        // 이미 삭제된 키보드를 삭제 요청 -> 예외 발생
        if (keyboard.isDeleted()) {
            throw new DataDuplicateException(ErrorCode.DUPLICATE_DELETED, "키보드");
        }

        // 삭제하는 유저와 생성한 유저한 동일한지 확인 -> 아니면 예외 발생
        if (!keyboard.getMember().getId().equals(memberId)) {
            throw new DataUnauthorizedAccessException(ErrorCode.FORBIDDEN_ACCESS, null);
        }

        keyboard.deleteKeyboard();
    }

    @Transactional(readOnly = true)
    public List<KeyboardReadResponseDto> findKeyboardBySellerId(Long sellerId) {

        List<Keyboard> keyboards = keyboardRepository.findAllByMemberIdAndIsDeletedFalse(sellerId);

        return keyboards.stream()
                .map(KeyboardReadResponseDto::of)
                .toList();
    }

    @Transactional
    public KeyboardUpdateResponseDto keyboardModification(Long sellerId, Long keyboardId,
            KeyboardUpdateRequestDto requestDto) {

        Keyboard findKeyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.NOT_FOUND_KEYBOARD, "키보드"));

        if (!findKeyboard.getMember().getId().equals(sellerId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS, null);
        }

        findKeyboard.updateKeyboard(requestDto);

        return KeyboardUpdateResponseDto.of(findKeyboard);
    }
}

