package com.deepnyangning.capstonebe.domain.studyroom.controller;


import com.deepnyangning.capstonebe.domain.studyroom.dto.*;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import com.deepnyangning.capstonebe.global.response.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

public interface StudyRoomReservationControllerDocs {
    @Operation(
            summary = "스터디룸 동반 이용자 존재 여부 확인 API",
            description = """
                    **스터디룸 동반 이용자 존재 여부 확인**
                                              
                    요청된 사용자가 실제 존재하는 사용자인지 확인합니다. \s
                    
                    **검증 내용**
                    
                    - 본인 자신을 동반 이용자로 등록할 수 없습니다.
                    - 사용자가 이미 해당 날짜에 예약을 했거나 다른 스터디룸에 동반 이용자로 등록되어 있으면 예외가 발생합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 필드**
                                        
                    - `identifier` : 사용자 학번 (예: "21011805")
                    - `name` : 사용자 이름 (예: "장윤정")
                    - `date` : 이용 날짜 (예: "2025-05-20")
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<Boolean>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "존재하는 사용자입니다." 또는 "일치하는 사용자가 존재하지 않습니다."
                        - `result`: 사용자 존재 여부 (true or false)
                    """
    )
    ResponseEntity<ApiResponse<Boolean>> getStudyRoomParticipant(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ParticipantRequest participantRequest);

    @Operation(
            summary = "스터디룸 예약 생성 API",
            description = """
                    **스터디룸 예약 생성**
                                              
                    사용자가 스터디룸 예약을 생성합니다. \s
                    예약 날짜, 시간, 목적 및 동반 이용자 정보를 포함합니다.
                    
                    **검증 내용**
                    - 예약 시작 시간과 종료 시간 간격은 60분 또는 120분이어야 합니다.
                    - 일요일에는 예약할 수 없습니다.
                    - 예약 가능 시간은 평일 10:00~21:00, 토요일 10:00~16:00 입니다.
                    - 예약 시간대가 기존 예약과 겹치면 예외가 발생합니다.
                    - 동반 이용자 수는 (스터디룸 최소 수용 인원 - 1) 이상이어야 합니다.
                    - 예약자 및 동반 이용자가 이미 해당 날짜에 예약 혹은 동반 이용자로 등록되어 있으면 예외가 발생합니다.
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 필드**
                                        
                    - `studyRoomId` : 예약할 스터디룸 ID (예: 3)
                    - `date` : 예약 날짜 (예: "2025-05-20")
                    - `startTime` : 예약 시작 시간 (예: "13:00")
                    - `endTime` : 예약 종료 시간 (예: "15:00")
                    - `purpose` : 예약 목적 (예: "스터디")
                    - `participants` : 동반 이용자 리스트
                        - `identifier` : 사용자 학번 (예: "21011806")
                        - `name` : 사용자 이름 (예: "정윤장")
                        - `date` : 이용 날짜 (예: "2025-05-20")
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<ReservationResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (201)
                        - `message`: "스터디룸 예약에 성공했습니다."
                        - `result`: 예약 정보 데이터
                            - `id`: 예약 ID (예: 10)
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CONFIRMED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<ReservationResponse>> createStudyRoomReservation(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ReservationRequest reservationRequest);

    @Operation(
            summary = "사용자별 스터디룸 예약 조회 API",
            description = """
                    **사용자별 스터디룸 예약 조회**
                                              
                    로그인한 사용자의 스터디룸 예약 내역을 조회합니다. \s
                    기본적으로 최근 예약 순으로 정렬되어 조회됩니다. \s
                    cursor 기반 페이지네이션을 지원합니다.
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 파라미터**
                                        
                    - `cursorDate` (optional) : 마지막으로 조회한 예약 날짜. 해당 날짜 이전의 예약 목록이 조회됩니다. (예: "2025-05-20")
                    - `size` (optional, default=7) : 조회할 예약 개수 (예: 7)
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<CursorPage<ReservationResponse>>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "사용자별 스터디룸 예약 조회에 성공했습니다."
                        - `result`: 예약 정보 데이터 목록
                            - `id`: 예약 ID (예: 10)
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CONFIRMED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<CursorPage<ReservationResponse>>> getStudyRoomReservationsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                                                @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate cursorDate,
                                                                                                @RequestParam(defaultValue = "7") int size);

    @Operation(
            summary = "스터디룸 예약 상세 조회 API",
            description = """
                    **스터디룸 예약 상세 조회**
                                              
                    예약 ID로 스터디룸 예약 상세 정보를 조회합니다. \s
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 경로 변수**
                                        
                    - `reservationId (Long)`: 스터디룸 예약 ID
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<ReservationResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 예약 조회에 성공했습니다."
                        - `result`: 예약 정보 데이터
                            - `id`: 예약 ID (예: 10)
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CONFIRMED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<ReservationResponse>> getStudyRoomReservation(@PathVariable Long reservationId);

    @Operation(
            summary = "스터디룸 예약 정보 수정 API",
            description = """
                    **스터디룸 예약 정보 수정**
                                              
                    예약 ID로 스터디룸 예약 정보를 수정합니다. \s
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 경로 변수**
                                        
                    - `reservationId (Long)`: 스터디룸 예약 ID
                    
                    
                    
                    **요청 필드**
                                        
                    - `date` : 예약 날짜 (예: "2025-05-20")
                    - `startTime` : 예약 시작 시간 (예: "13:00")
                    - `endTime` : 예약 종료 시간 (예: "15:00")
                    - `purpose` : 예약 목적 (예: "스터디")
                    - `participants` : 동반 이용자 리스트
                        - `identifier` : 사용자 학번 (예: "21011806")
                        - `name` : 사용자 이름 (예: "정윤장")
                        - `date` : 이용 날짜 (예: "2025-05-20")
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<ReservationResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 예약 변경에 성공했습니다."
                        - `result`: 변경된 예약 정보 데이터
                            - `id`: 예약 ID (예: 10)
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CONFIRMED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<ReservationResponse>> updateStudyRoomReservation(@PathVariable Long reservationId, @RequestBody ReservationUpdate reservationUpdate);

    @Operation(
            summary = "스터디룸 예약 취소 API",
            description = """
                    **스터디룸 예약 취소**
                                              
                    예약 ID로 스터디룸 예약을 취소합니다. \s
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 경로 변수**
                                        
                    - `reservationId (Long)`: 스터디룸 예약 ID
                    
                    
                             
                    **응답**
                                    
                    - `ApiResponse<Void>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 예약 취소에 성공했습니다."
                    """
    )
    ResponseEntity<ApiResponse<Void>> cancelStudyRoomReservation(@PathVariable Long reservationId);

    @Operation(
            summary = "관리자용 스터디룸 예약 전체 조회 API",
            description = """
                    **관리자용 스터디룸 예약 전체 조회**
                                              
                    스터디룸 예약 전체 목록을 커서 기반 페이징으로 조회합니다. \s
                    사용자 기준 조회와 달리 예약한 사용자 정보가 추가로 포함됩니다. \s
                    name 파라미터를 통해 스터디룸 이름으로 검색할 수 있으며, 띄어쓰기 여부와 관계없이 적용됩니다. \s
                    관리자만 접근 가능합니다. 
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 파라미터**
                                        
                    - `name` (optional) : 스터디룸 이름 필터링 (예: "03 스터디룸(4층)")
                    - `cursorDate` (optional) : 마지막으로 조회한 예약 날짜. 해당 날짜 이전의 예약 목록이 조회됩니다. (예: "2025-05-20")
                    - `cursorStartTime` (optional) : cursorDate가 같은 경우 해당 시간 이전의 예약이 조회됩니다. (예: "13:00")
                    - `size` (optional, default=7) : 조회할 예약 개수 (예: 7)
                                        
                                        
                                    
                    **응답**
                                    
                    - `ApiResponse<CursorPage<AdminReservationResponse>>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 예약 전체 조회에 성공했습니다."
                        - `result`: 예약 정보 목록
                            - `id`: 예약 ID (예: 10)
                            - `userInfo`: 예약한 사용자 정보
                                - `identifier`: 사용자 학번 (예: "21011805")
                                - `name`: 사용자 이름 (예: "장윤정")
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CONFIRMED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<CursorPage<AdminReservationResponse>>> getStudyRoomReservations(@RequestParam(required = false) String name,
                                                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate cursorDate,
                                                                                               @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime cursorStartTime,
                                                                                               @RequestParam(defaultValue = "7") int size);

    @Operation(
            summary = "관리자용 스터디룸 예약 상태 변경 API",
            description = """
                    **관리자용 스터디룸 예약 상태 변경**
                                              
                    예약 ID로 스터디룸 예약 상태를 변경합니다. \s
                    관리자만 접근 가능합니다.
          
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 경로 변수**
                                        
                    - `reservationId (Long)`: 스터디룸 예약 ID
                    
                    
                    
                    **요청 파라미터**
                                        
                    - `status` : 변경할 예약 상태 (예: "CONFIRMED" or "CANCELED" or "COMPLETED")
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<AdminReservationResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 예약 상태 변경에 성공했습니다."
                        - `result`: 예약 상태 변경된 예약 정보
                            - `id`: 예약 ID (예: 10)
                            - `userInfo`: 예약한 사용자 정보
                                - `identifier`: 사용자 학번 (예: "21011805")
                                - `name`: 사용자 이름 (예: "장윤정")
                            - `studyRoom`: 스터디룸 정보
                                - `id`: 스터디룸 ID (예: 3)
                                - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                                - `location`: 위치 정보 (예: "학술정보원 4층")
                                - `minCapacity`: 최소 수용 인원 (예: 3)
                                - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `date`: 예약 날짜 (예: "2025-05-20")
                            - `startTime`: 예약 시작 시간 (예: "13:00")
                            - `endTime`: 예약 종료 시간 (예: "15:00")
                            - `status`: 예약 상태 (예: "CANCELED")
                            - `purpose`: 예약 목적 (예: "스터디")
                            - `participants`: 동반 이용자 리스트
                                - `identifier`: 사용자 학번 (예: "21011806")
                                - `name`: 사용자 이름 (예: "정윤장")
                                - `date`: 이용 날짜 (예: "2025-05-20")
                    """
    )
    ResponseEntity<ApiResponse<AdminReservationResponse>> updateStudyRoomReservationStatus(@PathVariable Long reservationId, @RequestParam String status);

}
