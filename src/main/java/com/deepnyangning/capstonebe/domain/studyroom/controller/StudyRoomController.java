package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomSimpleResponse;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomMapper;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import com.deepnyangning.capstonebe.global.response.CursorPage;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "스터디룸 조회 API")
public class StudyRoomController implements StudyRoomControllerDocs {
    private final StudyRoomService studyRoomService;
    private final StudyRoomMapper studyRoomMapper;

    @GetMapping("/studyrooms/{id}")
    public ResponseEntity<ApiResponse<StudyRoomSimpleResponse>> getStudyRoom(@PathVariable Long id){
        StudyRoomSimpleResponse studyRoomResponse = studyRoomMapper.toSimpleDto(studyRoomService.findStudyRoomById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<StudyRoomSimpleResponse>builder().result(studyRoomResponse).success(true).code(200).message("스터디룸 상세 조회에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms")
    public ResponseEntity<ApiResponse<CursorPage<StudyRoomResponse>>> getAvailableStudyRooms(@RequestParam(required = false) LocalDate date,
                                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
                                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
                                                                                       @RequestParam(required = false) String cursorName,
                                                                                       @RequestParam(defaultValue = "7") int size){
        if(date == null) date = LocalDate.now();
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
            return ResponseEntity.ok(
                    ApiResponse.<CursorPage<StudyRoomResponse>>builder()
                            .result(CursorPage.of(List.of(), size))
                            .success(true)
                            .code(200)
                            .message("일요일은 스터디룸이 운영되지 않습니다.")
                            .build()
            );
        }
        List<StudyRoomResponse> response = studyRoomService.findAvailableStudyRooms(date, startTime, endTime, cursorName, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CursorPage<StudyRoomResponse>>builder().result(CursorPage.of(response, size)).success(true).code(200).message("예약 가능한 스터디룸 목록 조회에 성공했습니다.").build());
    }
}
