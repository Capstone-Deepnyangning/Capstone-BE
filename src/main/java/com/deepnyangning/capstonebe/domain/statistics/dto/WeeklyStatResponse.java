package com.deepnyangning.capstonebe.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyStatResponse {
    @Schema(description = "해당 주의 시작일 (월요일)", example = "2025-05-19")
    private LocalDate startDate;

    @Schema(description = "해당 주의 종료일 (일요일)", example = "2025-05-25")
    private LocalDate endDate;

    @Schema(description = "사용자 기준 주간 평균 체류 시간 (분 단위)", example = "240")
    private double weeklyAvg;

    @Schema(description = "전체 사용자 기준 주간 평균 체류 시간 (분 단위)", example = "180")
    private double globalAvg;

    @Schema(description = "사용자의 날짜별 체류 시간 목록")
    private List<DailyStayResponse> weeklyStay;
}
