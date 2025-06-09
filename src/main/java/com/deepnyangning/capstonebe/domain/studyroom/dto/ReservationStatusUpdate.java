package com.deepnyangning.capstonebe.domain.studyroom.dto;

import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationStatusUpdate {
    @Schema(description = "수정할 예약 상태", example = "CANCELED")
    private ReservationStatus status;
}
