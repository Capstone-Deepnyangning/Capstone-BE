package com.deepnyangning.capstonebe.domain.statistics.controller;

import com.deepnyangning.capstonebe.domain.statistics.dto.CongestionResponse;
import com.deepnyangning.capstonebe.domain.statistics.service.CongestionService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/congestion")
public class CongestionController {
    private final CongestionService congestionService;

    @GetMapping
    public ResponseEntity<ApiResponse<CongestionResponse>> getCongestionData(){
        CongestionResponse response = congestionService.getCongestionData();
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CongestionResponse>builder().result(response).success(true).code(200).message("혼잡도 조회에 성공했습니다.").build());
    }
}
