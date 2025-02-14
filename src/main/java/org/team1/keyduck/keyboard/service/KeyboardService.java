package org.team1.keyduck.keyboard.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.keyduck.keyboard.dto.response.KeyboardReadResponseDto;
import org.team1.keyduck.keyboard.entity.Keyboard;
import org.team1.keyduck.keyboard.repository.KeyboardRepository;

@Service
@RequiredArgsConstructor
public class KeyboardService {

    private final KeyboardRepository keyboardRepository;

    @Transactional(readOnly = true)
    public List<KeyboardReadResponseDto> findKeyboardBySellerIdService(Long sellerId) {

        List<Keyboard> keyboards = keyboardRepository.findAllByMemberId(sellerId);

        return keyboards.stream()
                .map(KeyboardReadResponseDto::of)
                .toList();
    }

    @Transactional
    public KeyboardUpdateResponseDto keyboardModificationService(Long sellerId, Long keyboardId, KeyboardUpdateRequestDto requestDto) {

        Keyboard findKeyboard = keyboardRepository.findById(keyboardId)
                .orElseThrow(() -> new DataNotFoundException(ErrorCode.KEYBOARD_NOT_FOUND));

        if (!findKeyboard.getMember().getId().equals(sellerId)) {
            throw new DataMismatchException(ErrorCode.FORBIDDEN_ACCESS);
        }

        findKeyboard.updateName(requestDto.getName());

        findKeyboard.updateDescriptions(requestDto.getDescription());

        Keyboard saveKeyboard = keyboardRepository.save(findKeyboard);

        return KeyboardUpdateResponseDto.of(saveKeyboard);
    }
}
