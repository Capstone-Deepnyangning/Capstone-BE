package com.deepnyangning.capstonebe.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStayResponse {
    @Schema(description = "날짜", example = "2025-05-20")
    private LocalDate date;

    @Schema(description = "해당 날짜의 체류 시간 (분 단위)", example = "180")
    private int stayMinutes;
}
