package org.team1.keyduck.keyboard.dto.response;

import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
public class KeyboardReadResponseDto {

    private final Long id;

    private final String name;

    private final String description;

    private KeyboardReadResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static KeyboardReadResponseDto of(Keyboard keyboard) {
        return new KeyboardReadResponseDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getDescription()
        );
    }
}
