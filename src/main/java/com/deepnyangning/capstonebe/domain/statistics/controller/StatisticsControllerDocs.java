package com.deepnyangning.capstonebe.domain.statistics.controller;

import com.deepnyangning.capstonebe.domain.statistics.dto.MonthlyStatResponse;
import com.deepnyangning.capstonebe.domain.statistics.dto.WeeklyStatResponse;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;

public interface StatisticsControllerDocs {
    @Operation(
            summary = "주간 통계 조회 API",
            description = """
                    **개인별 주간 통계 조회**
                                    
                    이번주 사용자의 도서관 이용 통계를 조회합니다. \s
                    월요일부터 일요일까지를 1주로 하며, 오늘이 월요일인 경우 해당 월요일의 데이터만 조회됩니다. \s
                                        
                    - 사용자 기준 이번 주 평균 체류 시간 (총 체류 시간 ÷ 7) 제공
                    - 전체 사용자 평균 체류 시간 제공
                    - 날짜별 체류 시간 목록 제공
                                        
                    ※ 날짜별 체류 시간 목록은 데이터가 존재하는 날짜만 포함됩니다.
                                                       
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<WeeklyStatResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "개인 주간 통계 조회에 성공했습니다."
                        - `result`: 주간 통계 데이터
                            - `startDate`: 해당 주 시작일 (월요일) (예: "2025-05-19")
                            - `endDate`: 해당 주 종료일 (일요일) (예: "2025-05-25")
                            - `weeklyAvg`: 요청 사용자 기준 주간 평균 체류 시간 (분 단위) (예: 240)
                            - `globalAvg`: 전체 사용자 기준 주간 평균 체류 시간 (분 단위) (예: 180)
                            - `weeklyStay`: 사용자의 날짜별 체류 시간 목록
                                - `date`: 날짜 (예: "2025-05-20")
                                - `stayMinutes`: 체류 시간 (분 단위) (예: 180)
                    """
    )
    public ResponseEntity<ApiResponse<WeeklyStatResponse>> getWeeklyStatistics(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "월별 통계 조회 API",
            description = """
                    **개인별 월별 통계 조회**
                                    
                    요청한 연도와 월에 해당하는 도서관 이용 통계를 조회합니다. \s
                                        
                    - 해당 월의 총 체류 시간 제공
                    - 사용자의 월 단위 날짜별 체류 시간 목록 제공
                                        
                    ※ 날짜별 체류 시간 목록은 데이터가 존재하는 날짜만 포함됩니다.
                                                       
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<MonthlyStatResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "개인 월별 통계 조회에 성공했습니다."
                        - `result`: 월별 통계 데이터
                            - `year`: 조회한 연도 (예: 2025)
                            - `month`: 조회한 월 (예: 5)
                            - `totalStay`: 해당 월 전체 체류 시간 (분 단위) (예: 3120)
                            - `monthlyStay`: 사용자의 날짜별 체류 시간 목록
                                - `date`: 날짜 (예: "2025-05-10")
                                - `stayMinutes`: 체류 시간 (분 단위) (예: 240)
                    """
    )
    public ResponseEntity<ApiResponse<MonthlyStatResponse>> getMonthlyStatistics(@AuthenticationPrincipal UserDetails userDetails,
                                                                                 @RequestParam int year,
                                                                                 @RequestParam int month);
}
