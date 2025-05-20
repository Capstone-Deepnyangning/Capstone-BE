package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogPreviewResponse {
    @Schema(description = "로그 ID", example = "1")
    private Long id;

    @Schema(description = "사용자 학번", example = "21011805")
    private String identifier;

    @Schema(description = "출입 유형", example = "ENTRY")
    private AccessType accessType;

    @Schema(description = "인증 방식", example = "FACE")
    private AuthMethod authMethod;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "출입 시간", example = "2024-05-20T08:32:15")
    private LocalDateTime accessTime;
}
