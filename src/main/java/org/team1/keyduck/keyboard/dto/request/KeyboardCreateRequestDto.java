package org.team1.keyduck.keyboard.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeyboardCreateRequestDto {

    @NotNull(message = "회원 Id값을 입력해주세요.")
    private Long memberId;

    @NotBlank(message = "상품 이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    private String description;


}
