package org.team1.keyduck.keyboard.dto.response;

import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
public class KeyboardCreateResponseDto {
    private Long id;
    private String name;
    private String description;

    private KeyboardCreateResponseDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public static KeyboardCreateResponseDto of(Keyboard keyboard) {
        return new KeyboardCreateResponseDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getDescription()
        );
    }
}