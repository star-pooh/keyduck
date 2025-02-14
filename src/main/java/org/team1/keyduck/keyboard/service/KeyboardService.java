package org.team1.keyduck.keyboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.dto.response.KeyboardDeleteResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;

    // 키보드 삭제
    @Transactional
    public KeyboardDeleteResponseDto deleteKeyboard(Long keyboardId) {
        // 저장한 유저아이디 가져오기(추후 토큰 적용)

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미 삭제된 게시글 예외 처리
        if (keyboard.isDeleted()) {
            throw new DuplicateDataException(ErrorCode.DUPLICATE_DELETED);
        }

        keyboard.softDelete();
        keyboardRepository.save(keyboard);

        return KeyboardDeleteResponseDto.of(keyboard);
    }

}
