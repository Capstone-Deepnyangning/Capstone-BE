package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.domain.face.dto.FaceIssueReportResponse;
import com.deepnyangning.capstonebe.domain.face.service.FaceIssueReportService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "안면 인식 문제 신고 API")
public class FaceIssueReportController implements FaceIssueReportControllerDocs {
    private final FaceIssueReportService faceIssueReportService;

    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<FaceIssueReportResponse>> saveFaceIssueReport(@AuthenticationPrincipal UserDetails userDetails){
        String identifier = userDetails.getUsername();
        FaceIssueReportResponse response = faceIssueReportService.saveFaceIssueReport(identifier);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<FaceIssueReportResponse>builder().result(response).success(true).code(201).message("안면 인식 문제 신고가 성공적으로 접수되었습니다.").build());
    }

    @GetMapping("/admin/reports")
    public ResponseEntity<ApiResponse<Page<FaceIssueReportResponse>>> getFaceIssueReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<FaceIssueReportResponse> response = faceIssueReportService.findFaceIssueReports(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<FaceIssueReportResponse>>builder().result(response).success(true).code(200).message("안면 인식 문제 신고 목록을 성공적으로 조회했습니다.").build());
    }

    @GetMapping("/admin/reports/{reportId}")
    public ResponseEntity<ApiResponse<FaceIssueReportResponse>> getFaceIssueReport(@PathVariable Long reportId){
        FaceIssueReportResponse response = faceIssueReportService.findFaceIssueReport(reportId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<FaceIssueReportResponse>builder().result(response).success(true).code(200).message("안면 인식 문제 신고 상세 정보를 성공적으로 조회했습니다.").build());
    }
}
