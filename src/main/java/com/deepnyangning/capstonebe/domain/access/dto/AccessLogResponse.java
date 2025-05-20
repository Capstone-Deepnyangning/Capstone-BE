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
public class AccessLogResponse {
    @Schema(description = "로그 ID", example = "3")
    private Long id;

    @Schema(description = "사용자 정보")
    private AccessLogUserInfo userInfo;

    @Schema(description = "출입 유형", example = "EXIT")
    private AccessType accessType;

    @Schema(description = "인증 방식", example = "QR")
    private AuthMethod authMethod;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(description = "출입 시간", example = "2024-05-20T09:12:45")
    private LocalDateTime accessTime;

    @Schema(description = "유사도 (얼굴 인증 시에만 존재)", example = "0.98")
    private double similarity;
}
