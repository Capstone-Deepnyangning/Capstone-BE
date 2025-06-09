package com.deepnyangning.capstonebe.domain.face.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceIssuePreviewResponse {
    @Schema(description = "신고 ID", example = "5")
    private Long id;

    @Schema(description = "사용자 학번", example = "21011805")
    private String identifier;

    @Schema(description = "관리자 확인 여부", example = "false")
    private boolean read;

    @Schema(description = "신고 생성 시간", example = "2024-05-20T09:12:45")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "신고 수정 시간", example = "2024-05-20T09:12:45")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

}
