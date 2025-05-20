package com.deepnyangning.capstonebe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdate {
    @Schema(description = "현재 비밀번호", example = "oldPassword123")
    @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
    String currentPassword;

    @Schema(description = "새 비밀번호", example = "newPassword456")
    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    String newPassword;
}
