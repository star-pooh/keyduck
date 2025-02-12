package org.team1.keyduck.keyboard.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KeyboardCreateRequestDto {

    @NotNull(message = "")
    private Long memberId;

    @NotNull(message = "상품 이름을 입력해주세요.")
    private String name;

    @NotNull(message = "상품 설명을 입력해주세요.")
    private String description;


}