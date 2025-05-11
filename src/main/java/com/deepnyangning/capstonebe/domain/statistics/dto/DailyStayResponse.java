package com.deepnyangning.capstonebe.domain.statistics.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStayResponse {
    private LocalDate date;
    private int stayMinutes;
}
