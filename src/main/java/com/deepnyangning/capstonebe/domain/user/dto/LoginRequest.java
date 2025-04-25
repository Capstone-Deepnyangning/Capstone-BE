package com.deepnyangning.capstonebe.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class LoginRequest {
    @NotBlank(message = "학번은 필수 입력값입니다.")
    private String identifier;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}
