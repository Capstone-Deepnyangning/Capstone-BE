package com.deepnyangning.capstonebe.domain.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeOption {
    @Schema(description = "예약 시작 가능한 시간", example = "10:00")
    private LocalTime start;

    @Schema(description = "해당 시작 시간 기준으로 예약 가능한 종료 시간 목록", example = "[\"11:00\", \"12:00\"]")
    private List<LocalTime> end;
}
