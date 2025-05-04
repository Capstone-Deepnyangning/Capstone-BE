package com.deepnyangning.capstonebe.domain.face.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceIssueReportResponse {
    private Long id;

    private Long userId;

    private boolean read;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
