package org.team1.keyduck.keyboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
@AllArgsConstructor
public class KeyboardCreateResponseDto {
    private Long id;
    private String name;
    private String description;

    public static KeyboardCreateResponseDto of(Keyboard keyboard) {
        return new KeyboardCreateResponseDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getDescription()
        );
    }
}
