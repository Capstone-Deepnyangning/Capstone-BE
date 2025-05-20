package com.deepnyangning.capstonebe.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatResponse {
    @Schema(description = "조회한 연도", example = "2025")
    private int year;

    @Schema(description = "조회한 월", example = "5")
    private int month;

    @Schema(description = "해당 월 전체 체류 시간 (분 단위)", example = "3120")
    private int totalStay;

    @Schema(description = "사용자의 날짜별 체류 시간 목록")
    private List<DailyStayResponse> monthlyStay;
}
