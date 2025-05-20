package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogPreviewResponse;
import com.deepnyangning.capstonebe.domain.access.dto.AccessLogResponse;
import com.deepnyangning.capstonebe.domain.access.dto.FailLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public interface LogControllerDocs {
    @Operation(
            summary = "출입 로그 조회 API",
            description = """
                    **관리자 출입 로그 조회**
                                    
                    출입 로그를 필터 조건에 따라 페이지 단위로 조회합니다. \s
                    관리자 권한이 필요합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                    
                    
                                    
                    **요청 파라미터**
                                    
                    - `LocalDateTime startTime` : 시작 시간 (옵션)
                    - `LocalDateTime endTime` : 종료 시간 (옵션)
                    - `String identifier` : 사용자 학번 (옵션)
                    - `String name` : 사용자 이름 (옵션)
                    - `AuthMethod authMethod` : 인증 방식 ("FACE" or "QR") (옵션)
                    - `int page` : 페이지 번호 (기본값: 0)
                    - `int size` : 페이지 크기 (기본값: 7)
                        
                                    
                                    
                    **응답**
                                    
                    - `ApiResponse<Page<AccessLogPreviewResponse>>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "출입 로그 목록을 성공적으로 조회했습니다."
                        - `result` : 페이지네이션된 출입 로그 데이터
                            - `content`: 출입 로그 배열
                                - `id`: 로그 ID (예: 1)
                                - `identifier`: 사용자 학번 (예: "21011805")
                                - `accessType`: 출입 유형 ("ENTRY" or "EXIT")
                                - `authMethod`: 인증 방식 ("FACE" or "QR")
                                - `accessTime`: 출입 시간 (예: "2024-05-20T08:32:15")
                            - `totalElements`: 전체 항목 수 (예: 25)
                            - `totalPages`: 전체 페이지 수 (예: 4)
                            - `size`: 페이지 크기 (예: 7)
                            - `number`: 현재 페이지 번호 (예: 0)
                    """
    )
    public ResponseEntity<ApiResponse<Page<AccessLogPreviewResponse>>> getAccessLogs(@RequestParam(required = false) LocalDateTime startTime,
                                                                                     @RequestParam(required = false) LocalDateTime endTime,
                                                                                     @RequestParam(required = false) String identifier,
                                                                                     @RequestParam(required = false) String name,
                                                                                     @RequestParam(required = false) AuthMethod authMethod,
                                                                                     @RequestParam(defaultValue = "0") int page,
                                                                                     @RequestParam(defaultValue = "7") int size);

    @Operation(
            summary = "출입 로그 상세 조회 API",
            description = """
                    **관리자 출입 로그 상세 조회**
                                    
                    특정 출입 로그 ID에 대한 상세 정보를 조회합니다. \s
                    관리자 권한이 필요합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                          
                          
                                   
                    **요청 경로 변수**
                                    
                    - `Long logId` : 출입 로그 ID
                        
                                    
                    
                    **응답**
                    
                    - `ApiResponse<AccessLogResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "출입 로그 상세 정보를 성공적으로 조회했습니다."
                        - `result`: 출입 로그 상세 데이터
                            - `id`: 로그 ID (예: 3)
                            - `userInfo`: 사용자 정보
                                - `identifier`: 학번 (예: "21011805")
                                - `name`: 이름 (예: "장윤정")
                            - `accessType`: 출입 유형 ("ENTRY" or "EXIT")
                            - `authMethod`: 인증 방식 ("FACE" or "QR")
                            - `accessTime`: 출입 시간 (예: "2024-05-20T09:12:45")
                            - `similarity`: 유사도 (얼굴 인식 시에만 존재, 예: 0.98)                                                
                    """
    )
    public ResponseEntity<ApiResponse<AccessLogResponse>> getAccessLog(@PathVariable Long logId);

    @Operation(
            summary = "출입 실패 로그 조회 API",
            description = """
                    **관리자 출입 실패 로그 조회**
                                    
                    출입 인증에 실패한 로그를 조건에 따라 조회합니다. \s
                    관리자 권한이 필요합니다.
                               
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                       
                    
                        
                    **요청 파라미터**
                    - `LocalDateTime startTime` : 시작 시간 (옵션)
                    - `LocalDateTime endTime` : 종료 시간 (옵션)
                    - `AuthMethod authMethod` : 인증 방식 ("FACE" or "QR") (옵션)
                    - `int page` : 페이지 번호 (기본값: 0)
                    - `int size` : 페이지 크기 (기본값: 7)        
                    
                    
                    
                    **응답**
                    
                    - `ApiResponse<Page<FailLogResponse>>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "인증 실패 로그 목록을 성공적으로 조회했습니다."
                        - `result` : 출입 실패 로그 목록 데이터
                            - `content`: 실패 로그 배열
                                - `id`: 로그 ID (예: 12)
                                - `identifier`: 사용자 학번 (예: "21012567")
                                - `authMethod`: 인증 방식 ("FACE" or "QR")
                                - `failTime`: 실패 시각 (예: "2024-05-20T10:23:58")
                                - `failReason`: 실패 사유 (예: "얼굴 인식 실패")
                            - `totalElements`: 전체 항목 수 (예: 8)
                            - `totalPages`: 전체 페이지 수 (예: 2)
                            - `size`: 페이지 크기 (예: 7)
                            - `number`: 현재 페이지 번호 (예: 0)                                          
                    """
    )
    public ResponseEntity<ApiResponse<Page<FailLogResponse>>> getFailLogs(@RequestParam(required = false) LocalDateTime startTime,
                                                                          @RequestParam(required = false) LocalDateTime endTime,
                                                                          @RequestParam(required = false) AuthMethod authMethod,
                                                                          @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "7") int size);
}
