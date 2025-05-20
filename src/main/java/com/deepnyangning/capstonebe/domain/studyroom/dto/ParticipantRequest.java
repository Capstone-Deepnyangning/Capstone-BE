package com.deepnyangning.capstonebe.domain.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequest {
    @Schema(description = "사용자 학번", example = "21011806")
    private String identifier;

    @Schema(description = "사용자 이름", example = "정윤장")
    private String name;

    @Schema(description = "이용 날짜", example = "2025-05-20")
    private LocalDate date;
}
