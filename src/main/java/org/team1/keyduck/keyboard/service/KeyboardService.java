package org.team1.keyduck.keyboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;

    // 키보드 삭제
    @Transactional
    public void deleteKeyboard(Long keyboardId) {
        // 저장한 유저아이디 가져오기(추후 토큰 적용)

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        keyboard.softDelete();
        keyboardRepository.save(keyboard);
    }

}
