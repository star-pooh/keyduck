package org.team1.keyduck.keyboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyboardDeleteResponseDto {
    private Long keyboardId;
    private String name;
    private String description;
    private boolean isDeleted;
}