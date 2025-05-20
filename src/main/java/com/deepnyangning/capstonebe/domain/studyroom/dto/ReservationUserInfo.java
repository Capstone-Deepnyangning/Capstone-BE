package com.deepnyangning.capstonebe.domain.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUserInfo {
    @Schema(description = "예약자 학번", example = "21011806")
    private String identifier;

    @Schema(description = "예약자 이름", example = "정윤장")
    private String name;
}
