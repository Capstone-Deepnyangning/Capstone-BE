package com.deepnyangning.capstonebe.domain.statistics.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyStatResponse {
    private LocalDate startDate;

    private LocalDate endDate;

    private double weeklyAvg;

    private double globalAvg;

    private List<DailyStayResponse> weeklyStay;
}
