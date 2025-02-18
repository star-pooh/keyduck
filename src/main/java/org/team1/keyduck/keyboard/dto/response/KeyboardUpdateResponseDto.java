package org.team1.keyduck.keyboard.dto.response;

import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
public class KeyboardUpdateResponseDto {

    private final String name;

    private final String description;

    private KeyboardUpdateResponseDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static KeyboardUpdateResponseDto of(Keyboard keyboard) {
        return new KeyboardUpdateResponseDto(
                keyboard.getName(),
                keyboard.getDescription()
        );
    }
}
