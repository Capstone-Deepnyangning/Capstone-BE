package com.deepnyangning.capstonebe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "학번은 필수 입력값입니다.")
    @Pattern(regexp = "^[0-9]+$", message = "학번은 숫자만 입력할 수 있습니다.")
    @Schema(example = "21011805")
    private String identifier;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Schema(example = "password")
    private String password;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Schema(example = "장윤정")
    private String name;

    @NotBlank(message = "역할은 필수 입력값입니다.")
    @Schema(example = "USER")
    private String role;
}
