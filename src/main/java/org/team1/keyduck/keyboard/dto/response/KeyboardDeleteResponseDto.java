package org.team1.keyduck.keyboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.team1.keyduck.keyboard.entity.Keyboard;

@Getter
@AllArgsConstructor
public class KeyboardDeleteResponseDto {
    private Long keyboardId;
    private String name;
    private String description;
    private boolean isDeleted;

    public static KeyboardDeleteResponseDto of(Keyboard keyboard) {
        return new KeyboardDeleteResponseDto(
                keyboard.getId(),
                keyboard.getName(),
                keyboard.getDescription(),
                keyboard.isDeleted()
        );
    }
}