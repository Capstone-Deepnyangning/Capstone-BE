package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogPreviewResponse;
import com.deepnyangning.capstonebe.domain.access.dto.AccessLogResponse;
import com.deepnyangning.capstonebe.domain.access.dto.FailLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.service.LogService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/logs")
public class LogController {
    private final LogService logService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<AccessLogPreviewResponse>>> getAccessLogs(@RequestParam(required = false) LocalDateTime startTime,
                                                                                     @RequestParam(required = false) LocalDateTime endTime,
                                                                                     @RequestParam(required = false) String identifier,
                                                                                     @RequestParam(required = false) String name,
                                                                                     @RequestParam(required = false) AuthMethod authMethod,
                                                                                     @RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "accessTime"));
        Page<AccessLogPreviewResponse> response = logService.findAccessLogs(startTime, endTime, identifier, name, authMethod, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<AccessLogPreviewResponse>>builder().result(response).success(true).code(200).message("출입 로그 목록을 성공적으로 조회했습니다.").build());
    }

    @GetMapping("/{logId}")
    public ResponseEntity<ApiResponse<AccessLogResponse>> getAccessLog(@PathVariable Long logId){
        AccessLogResponse response = logService.findAccessLog(logId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AccessLogResponse>builder().result(response).success(true).code(200).message("출입 로그 상세 정보를 성공적으로 조회했습니다.").build());
    }

    @GetMapping("/failure")
    public ResponseEntity<ApiResponse<Page<FailLogResponse>>> getFailLogs(@RequestParam(required = false) LocalDateTime startTime,
                                                                          @RequestParam(required = false) LocalDateTime endTime,
                                                                          @RequestParam(required = false) AuthMethod authMethod,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "accessTime"));
        Page<FailLogResponse> response = logService.findFailLogs(startTime, endTime, authMethod, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<FailLogResponse>>builder().result(response).success(true).code(200).message("인증 실패 로그 목록을 성공적으로 조회했습니다.").build());
    }
}
