package com.deepnyangning.capstonebe.domain.statistics.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyStatResponse {
    private int year;

    private int month;

    private int totalStay;

    private List<DailyStayResponse> monthlyStay;
}
