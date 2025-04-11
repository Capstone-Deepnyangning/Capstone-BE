package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.dto.StudyRoomResponse;
import com.deepnyangning.capstonebe.domain.studyroom.mapper.StudyRoomMapper;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudyRoomController {
    private final StudyRoomService studyRoomService;
    private final StudyRoomMapper studyRoomMapper;

    @GetMapping("/studyrooms/{id}")
    public ResponseEntity<ApiResponse<StudyRoomResponse>> getStudyRoom(@PathVariable Long id){
        StudyRoomResponse studyRoomResponse = studyRoomMapper.toResponseDto(studyRoomService.findStudyRoomById(id));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<StudyRoomResponse>builder().result(studyRoomResponse).success(true).code(200).message("스터디룸 상세 조회에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms")
    public ResponseEntity<ApiResponse<Page<StudyRoomResponse>>> getAvailableStudyRooms(@RequestParam(required = false) LocalDate date,
                                                                                       @RequestParam(required = false) LocalTime startTime,
                                                                                       @RequestParam(required = false) LocalTime endTime,
                                                                                       @RequestParam(defaultValue = "0") int page,
                                                                                       @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<StudyRoomResponse> studyRoomResponses = studyRoomService.findAvailableStudyRooms(date, startTime, endTime, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<StudyRoomResponse>>builder().result(studyRoomResponses).success(true).code(200).message("스터디룸 조회에 성공했습니다.").build());
    }
}
