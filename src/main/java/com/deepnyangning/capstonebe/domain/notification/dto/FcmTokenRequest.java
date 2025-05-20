package com.deepnyangning.capstonebe.domain.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmTokenRequest {
    @Schema(description = "FCM 토큰 값", example = "f7qU5a2WSsyL_abc123xyzTOKEN")
    private String token;
}
