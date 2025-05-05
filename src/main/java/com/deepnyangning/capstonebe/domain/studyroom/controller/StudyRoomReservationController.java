package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.dto.ParticipantRequest;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationRequest;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationResponse;
import com.deepnyangning.capstonebe.domain.studyroom.dto.ReservationUpdate;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomParticipantService;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomReservationService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StudyRoomReservationController {
    private final StudyRoomReservationService reservationService;
    private final StudyRoomParticipantService participantService;


    @PostMapping("/studyrooms/participants")
    public ResponseEntity<ApiResponse<Boolean>> getStudyRoomParticipant(@RequestBody ParticipantRequest participantRequest){
        boolean exists = participantService.existsParticipant(participantRequest);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Boolean>builder().result(exists).success(true).code(200).message("존재하는 사용자입니다.").build());
    }
    // participantResponse로 응답하는 거 고려하기

    @PostMapping("/studyrooms/reservations")
    public ResponseEntity<ApiResponse<ReservationResponse>> createStudyRoomReservation(@RequestBody ReservationRequest reservationRequest){
        ReservationResponse reservationResponse = reservationService.saveReservation(reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(201).message("스터디룸 예약에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms/reservations/users/{userId}")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getStudyRoomReservationsByUser(@PathVariable Long userId,
                                                                                                 @RequestParam(defaultValue = "0") int page,
                                                                                                 @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<ReservationResponse> reservationResponses = reservationService.findReservationsByUser(userId, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<ReservationResponse>>builder().result(reservationResponses).success(true).code(200).message("사용자별 스터디룸 예약 조회에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getStudyRoomReservation(@PathVariable Long reservationId){
        ReservationResponse reservationResponse = reservationService.findReservationById(reservationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(200).message("스터디룸 예약 조회에 성공했습니다.").build());
    }

    @PutMapping("/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateStudyRoomReservation(@PathVariable Long reservationId, @RequestBody ReservationUpdate reservationUpdate){
        ReservationResponse reservationResponse = reservationService.updateReservation(reservationId, reservationUpdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(200).message("스터디룸 예약 변경에 성공했습니다.").build());
    }

    @DeleteMapping("/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<Void>> cancelStudyRoomReservation(@PathVariable Long reservationId){
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message("스터디룸 예약 취소에 성공했습니다.").build());
    }

    @GetMapping("/admin/studyrooms/reservations")
    public ResponseEntity<ApiResponse<Page<ReservationResponse>>> getStudyRoomReservations(@RequestParam(required = false) String name,
                                                                                           @RequestParam(defaultValue = "0") int page,
                                                                                           @RequestParam(defaultValue = "7") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("date"), Sort.Order.desc("startTime")));
        Page<ReservationResponse> reservationResponses = reservationService.findReservationsByStudyRoomName(name, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Page<ReservationResponse>>builder().result(reservationResponses).success(true).code(200).message("스터디룸 예약 전체 조회에 성공했습니다.").build());
    }

    @PutMapping("/admin/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> updateStudyRoomReservationStatus(@PathVariable Long reservationId, @RequestParam String status){
        ReservationResponse reservationResponse = reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(200).message("스터디룸 예약 상태 변경에 성공했습니다.").build());
    }
}
