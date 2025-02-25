package org.team1.keyduck.email.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.keyduck.common.util.ValidationErrorMessage;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MultipleEmailRequestDto {

    private List<Long> memberIds;

    @NotBlank(message = ValidationErrorMessage.EMAIL_TITLE_IS_NOT_NULL)
    private String emailTitle;

    @NotBlank(message = ValidationErrorMessage.EMAIL_CONTENT_IS_NOT_NULL)
    private String emailContent;
}
