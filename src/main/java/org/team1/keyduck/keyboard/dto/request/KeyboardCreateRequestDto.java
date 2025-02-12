package org.team1.keyduck.keyboard.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeyboardCreateRequestDto {
    private Long memberId;
    private String name;
    private String description;
}