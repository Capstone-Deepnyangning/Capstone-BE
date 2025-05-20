package com.deepnyangning.capstonebe.domain.statistics.controller;

import com.deepnyangning.capstonebe.domain.statistics.dto.CongestionResponse;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;

public interface CongestionControllerDocs {
    @Operation(
            summary = "혼잡도 조회 API",
            description = """
                    **도서관 혼잡도 조회**
                                    
                    조회 요청 시간 기준의 혼잡도 데이터를 조회합니다. \s
                    현재 도서관 이용자 수와 최근 일주일 간의 평균 이용자 수 정보가 제공됩니다.
                                                       
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<CongestionResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "혼잡도 조회에 성공했습니다."
                        - `result`: 혼잡도 데이터
                          - `currentUsers`: 현재 이용자 수 (예: 503)
                          - `avgUsers`: 평균 이용자 수 (예: 452)
                    """
    )
    public ResponseEntity<ApiResponse<CongestionResponse>> getCongestionData();
}
