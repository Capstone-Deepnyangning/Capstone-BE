package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomSimpleResponse;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import com.deepnyangning.capstonebe.global.response.CursorPage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

public interface StudyRoomControllerDocs {
    @Operation(
            summary = "스터디룸 상세 조회 API",
            description = """
                    **스터디룸 상세 조회**
                                              
                    스터디룸의 기본 정보를 단건으로 조회합니다. \s
                    예약 가능 시간 등은 포함되지 않으며, 단순한 위치/수용 인원 등의 정보만 반환됩니다.
                                        
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 경로 변수**
                                        
                    - `id (Long)`: 스터디룸 ID
                                        
                                        
                                    
                    **응답**
                                    
                    - `ApiResponse<StudyRoomSimpleResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "스터디룸 상세 조회에 성공했습니다."
                        - `result`: 스터디룸 정보 데이터
                            - `id`: 스터디룸 ID (예: 3)
                            - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                            - `location`: 위치 정보 (예: "학술정보원 4층")
                            - `minCapacity`: 최소 수용 인원 (예: 3)
                            - `maxCapacity`: 최대 수용 인원 (예: 6)
                    """
    )
    public ResponseEntity<ApiResponse<StudyRoomSimpleResponse>> getStudyRoom(@PathVariable Long id);

    @Operation(
            summary = "예약 가능한 스터디룸 목록 조회 API",
            description = """
                    **예약 가능한 스터디룸 목록 조회**
                                              
                    특정 날짜/시간 기준으로 예약 가능한 스터디룸 목록을 조회합니다. \s
                    일요일에는 스터디룸이 운영되지 않으며, 빈 리스트와 안내 메시지가 반환됩니다. \s
                    커서 기반 페이지네이션이 적용됩니다.
                         
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                          
                                        
                        
                    **요청 파라미터**
                                        
                    - `date (optional, LocalDate)`: 조회할 날짜 (예: 2025-05-20), 기본값은 오늘 날짜
                    - `startTime (optional, HH:mm)`: 원하는 시작 시간 (예: 13:00)
                    - `endTime (optional, HH:mm)`: 원하는 종료 시간 (예: 15:00)
                    - `cursorName (optional)`: 커서 기준이 되는 스터디룸 이름 (다음으로 조회할 내용의 직전 스터디룸 이름)
                    - `size (optional, default=7)`: 한 페이지에 불러올 데이터 개수
                    
                    
                                    
                    **응답**
                                    
                    - `ApiResponse<StudyRoomSimpleResponse>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "예약 가능한 스터디룸 목록 조회에 성공했습니다." 또는 "일요일은 스터디룸이 운영되지 않습니다."
                        - `result`: 스터디룸 정보 데이터와 해당 날짜 예약 현황 그래프
                            - `id`: 스터디룸 ID (예: 3)
                            - `name`: 스터디룸 이름 (예: "03 스터디룸(4층)")
                            - `location`: 위치 정보 (예: "학술정보원 4층")
                            - `minCapacity`: 최소 수용 인원 (예: 3)
                            - `maxCapacity`: 최대 수용 인원 (예: 6)
                            - `reservedTimes`: Map<Integer, Boolean> 형태의 예약 가능 시간 정보 (예: {9: true, 10: false, ...})
                        - `hasNext`: 다음 페이지 존재 여부
                        - `nextCursor`: 다음 페이지 요청 시 사용할 cursorName
                    """
    )
    public ResponseEntity<ApiResponse<CursorPage<StudyRoomResponse>>> getAvailableStudyRooms(@RequestParam(required = false) LocalDate date,
                                                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                                                                             @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                                                                             @RequestParam(required = false) String cursorName,
                                                                                             @RequestParam(defaultValue = "7") int size);
}
