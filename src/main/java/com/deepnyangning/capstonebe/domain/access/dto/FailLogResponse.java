package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailLogResponse {
    @Schema(description = "로그 ID", example = "12")
    private Long id;

    @Schema(description = "인증 방식", example = "FACE")
    private AuthMethod authMethod;

    @Schema(description = "인증 실패 시각", example = "2024-05-20T10:23:58")
    private LocalDateTime accessTime;

    @Schema(description = "유사도 (얼굴 인증 시에만 존재)", example = "0.421")
    private Double similarity;
}
