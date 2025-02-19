package org.team1.keyduck.keyboard.dto.response;

import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
public class KeyboardDto {

    private final Long id;

    private final String name;

    private final String description;

    private KeyboardDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static KeyboardDto of(Keyboard keyboard) {
        return new KeyboardDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getDescription()
        );
    }
}
