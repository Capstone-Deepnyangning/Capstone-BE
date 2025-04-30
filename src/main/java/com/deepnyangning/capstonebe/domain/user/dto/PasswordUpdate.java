package com.deepnyangning.capstonebe.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PasswordUpdate {
    @NotBlank(message = "현재 비밀번호를 입력해 주세요.")
    String currentPassword;

    @NotBlank(message = "새 비밀번호를 입력해 주세요.")
    String newPassword;
}
