package com.deepnyangning.capstonebe.domain.statistics.controller;

import com.deepnyangning.capstonebe.domain.statistics.dto.MonthlyStatResponse;
import com.deepnyangning.capstonebe.domain.statistics.dto.WeeklyStatResponse;
import com.deepnyangning.capstonebe.domain.statistics.service.StatisticsService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access/statistics")
@Tag(name = "개인 출입 통계 조회 API")
public class StatisticsController implements StatisticsControllerDocs {
    private final StatisticsService statisticsService;

    // 개인별 weekly 출입 통계
    @GetMapping
    public ResponseEntity<ApiResponse<WeeklyStatResponse>> getWeeklyStatistics(@AuthenticationPrincipal UserDetails userDetails){
        String identifier = userDetails.getUsername();
        WeeklyStatResponse response = statisticsService.getWeeklyStatistics(identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<WeeklyStatResponse>builder().result(response).success(true).code(200).message("개인 주간 통계 조회에 성공했습니다.").build());
    }
    // 개인별 monthly 출입 통계
    @GetMapping("/monthly")
    public ResponseEntity<ApiResponse<MonthlyStatResponse>> getMonthlyStatistics(@AuthenticationPrincipal UserDetails userDetails,
                                                                                 @RequestParam int year,
                                                                                 @RequestParam int month){
        String identifier = userDetails.getUsername();
        MonthlyStatResponse response = statisticsService.getMonthlyStatistics(identifier, year, month);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<MonthlyStatResponse>builder().result(response).success(true).code(200).message("개인 월별 통계 조회에 성공했습니다.").build());
    }
}
