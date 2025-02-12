package org.team1.keyduck.keyboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
@AllArgsConstructor
public class KeyboardReadResponseDto {

    private Long id;

    private String name;

    private String keyboardDetails;

    public static KeyboardReadResponseDto of(Keyboard keyboard) {
        return new KeyboardReadResponseDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getKeyboardDetails()
        );
    }
}
