package org.team1.keyduck.keyboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.common.exception.DataNotFoundException;
import org.team1.keyduck.common.exception.DataNotMatchException;
import org.team1.keyduck.common.exception.DuplicateDataException;
import org.team1.keyduck.common.exception.ErrorCode;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;

    // 키보드 삭제
    @Transactional
    public void deleteKeyboard(Long keyboardId, Long memberId) {

        Keyboard keyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.RESOURCE_NOT_FOUND));

        // 이미 삭제된 키보드를 삭제 요청 -> 예외 발생
        if (keyboard.isDeleted()) {
            throw new DuplicateDataException(ErrorCode.DUPLICATE_DELETED);
        }

        // 삭제하는 유저와 생성한 유저한 동일한지 확인 -> 아니면 예외 발생
        if (!keyboard.getMember().getId().equals(memberId)) {
            throw new DataNotMatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        keyboard.deleteKeyboard();
    }

}
