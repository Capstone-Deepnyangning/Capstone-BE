package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.domain.face.dto.FaceIssueReportResponse;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;

public interface FaceIssueReportControllerDocs {
    @Operation(
            summary = "안면 인식 문제 신고 API",
            description = """
                    **사용자의 안면 인식 문제 신고**
                                    
                    출입 과정에서 안면 인식이 정상적으로 작동하지 않았을 경우, 사용자가 문제를 직접 신고할 수 있습니다. \s
                    해당 신고는 관리자 페이지에서 확인할 수 있습니다.
                                                       
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<FaceIssueReportResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "안면 인식 문제 신고가 성공적으로 접수되었습니다."
                        - `result`: 신고 상세 데이터
                          - `id`: 신고 ID (예: 5)
                          - `userId`: 사용자 ID (예: 2)
                          - `read`: 관리자 확인 여부 (예: false)
                          - `createdAt`: 신고 생성 시간 (예: "2024-05-20T09:12:45")
                          - `updatedAt`: 신고 수정 시간 (예: "2024-05-20T09:12:45")
                    """
    )
    public ResponseEntity<ApiResponse<FaceIssueReportResponse>> saveFaceIssueReport(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "안면 인식 문제 신고 목록 조회 API",
            description = """
                    **전체 안면 인식 문제 신고 목록 조회**
                                    
                    등록된 모든 안면 인식 문제 신고 내역을 페이지네이션 방식으로 조회합니다. \s
                    읽음 표시 기능이 제공됩니다. \s
                    관리자 권한이 필요합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                                                  
                                                  
                                                       
                    **요청 파라미터**
                                    
                    - `page` : 페이지 번호 (기본값: 0)
                    - `size` : 페이지당 항목 수 (기본값: 7)
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<Page<FaceIssueReportResponse>>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "안면 인식 문제 신고 목록을 성공적으로 조회했습니다."
                        - `result`: 신고 목록 페이지 데이터
                          - `id`: 신고 ID (예: 5)
                          - `userId`: 사용자 ID (예: 2)
                          - `read`: 관리자 확인 여부 (예: false)
                          - `createdAt`: 신고 생성 시간 (예: "2024-05-20T09:12:45")
                          - `updatedAt`: 신고 수정 시간 (예: "2024-05-20T09:12:45")
                    """
    )
    public ResponseEntity<ApiResponse<Page<FaceIssueReportResponse>>> getFaceIssueReports(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "7") int size);

    @Operation(
            summary = "안면 인식 문제 신고 상세 조회 API",
            description = """
                    **특정 안면 인식 문제 신고 상세 조회**
                                    
                    신고 ID를 기반으로 특정 문제 신고의 상세 정보를 조회합니다. \s
                    해당 API를 호출 시 읽음 처리되어, 이후 요청 시 read 필드가 true로 표시됩니다. \s
                    관리자 권한이 필요합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                        
                        
                                                       
                    **요청 파라미터**
                                    
                    - `reportId` : 조회할 신고 ID (예: 5)
                          
                        
                                    
                    **응답**
                                    
                    - `ApiResponse<FaceIssueReportResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "안면 인식 문제 신고 상세 정보를 성공적으로 조회했습니다."
                        - `result`: 신고 상세 데이터
                          - `id`: 신고 ID (예: 5)
                          - `userId`: 사용자 ID (예: 2)
                          - `read`: 관리자 확인 여부 (예: true)
                          - `createdAt`: 신고 생성 시간 (예: "2024-05-20T09:12:45")
                          - `updatedAt`: 신고 수정 시간 (예: "2024-05-20T09:12:45")
                    """
    )
    public ResponseEntity<ApiResponse<FaceIssueReportResponse>> getFaceIssueReport(@RequestParam Long reportId);
}
