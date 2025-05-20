package com.deepnyangning.capstonebe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class LoginRequest {
    @Schema(description = "사용자 학번", example = "21011805")
    @NotBlank(message = "학번은 필수 입력값입니다.")
    private String identifier;

    @Schema(description = "사용자 비밀번호", example = "password")
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
