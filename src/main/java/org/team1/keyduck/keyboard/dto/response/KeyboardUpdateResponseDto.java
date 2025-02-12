package org.team1.keyduck.keyboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
@AllArgsConstructor
public class KeyboardUpdateResponseDto {

    private String name;

    private String keyboardDetails;

    public static KeyboardUpdateResponseDto of(Keyboard keyboard) {
        return new KeyboardUpdateResponseDto(
                keyboard.getName(),
                keyboard.getKeyboardDetails()
        );
    }

}
