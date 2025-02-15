package org.team1.keyduck.keyboard.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyboardUpdateRequestDto {

    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String description;

}
