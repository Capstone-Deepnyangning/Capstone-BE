package com.deepnyangning.capstonebe.domain.studyroom.controller;

import com.deepnyangning.capstonebe.domain.studyroom.dto.*;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomParticipantService;
import com.deepnyangning.capstonebe.domain.studyroom.service.StudyRoomReservationService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "스터디룸 예약 API")
public class StudyRoomReservationController implements StudyRoomReservationControllerDocs {
    private final StudyRoomReservationService reservationService;
    private final StudyRoomParticipantService participantService;


    @PostMapping("/studyrooms/participants")
    public ResponseEntity<ApiResponse<Boolean>> getStudyRoomParticipant(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ParticipantRequest participantRequest){
        String identifier = userDetails.getUsername();
        boolean exists = participantService.existsParticipant(identifier, participantRequest);
        String message = exists ? "존재하는 사용자입니다." : "일치하는 사용자가 존재하지 않습니다.";
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Boolean>builder().result(exists).success(true).code(200).message(message).build());
    }

    @PostMapping("/studyrooms/reservations")
    public ResponseEntity<ApiResponse<ReservationResponse>> createStudyRoomReservation(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ReservationRequest reservationRequest){
        String identifier = userDetails.getUsername();
        ReservationResponse reservationResponse = reservationService.saveReservation(identifier, reservationRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(201).message("스터디룸 예약에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms/reservations/my")
    public ResponseEntity<ApiResponse<CursorPage<ReservationResponse>>> getStudyRoomReservationsByUser(@AuthenticationPrincipal UserDetails userDetails,
                                                                                                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate cursorDate,
                                                                                                       @RequestParam(defaultValue = "7") int size){
        String identifier = userDetails.getUsername();
        List<ReservationResponse> response = reservationService.findReservationsByUser(identifier, cursorDate, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CursorPage<ReservationResponse>>builder().result(CursorPage.of(response, size)).success(true).code(200).message("사용자별 스터디룸 예약 조회에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<ReservationResponse>> getStudyRoomReservation(@PathVariable Long reservationId){
        ReservationResponse reservationResponse = reservationService.findReservationById(reservationId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<ReservationResponse>builder().result(reservationResponse).success(true).code(200).message("스터디룸 예약 조회에 성공했습니다.").build());
    }

    @GetMapping("/studyrooms/reservations/available-time")
    public ResponseEntity<ApiResponse<List<AvailableTimeOption>>> getStudyRoomReservationAvailableTimes(@RequestParam Long studyRoomId, @RequestParam LocalDate date){
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.<List<AvailableTimeOption>>builder().result(List.of()).success(true).code(200).message("일요일은 스터디룸이 운영되지 않습니다.").build());
        }
        List<AvailableTimeOption> response = reservationService.findAvailableTimes(studyRoomId, date);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<List<AvailableTimeOption>>builder().result(response).success(true).code(200).message("스터디룸별 예약 가능한 시간 조회에 성공했습니다.").build());
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
    public ResponseEntity<ApiResponse<CursorPage<AdminReservationResponse>>> getStudyRoomReservations(@RequestParam(required = false) String name,
                                                                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate cursorDate,
                                                                                                      @RequestParam(required = false) @DateTimeFormat(pattern = "HH:mm") LocalTime cursorStartTime,
                                                                                                      @RequestParam(defaultValue = "7") int size){
        List<AdminReservationResponse> response = reservationService.findReservationsByStudyRoomName(name, cursorDate, cursorStartTime, size);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<CursorPage<AdminReservationResponse>>builder().result(CursorPage.of(response, size)).success(true).code(200).message("스터디룸 예약 전체 조회에 성공했습니다.").build());
    }

    @PutMapping("/admin/studyrooms/reservations/{reservationId}")
    public ResponseEntity<ApiResponse<AdminReservationResponse>> updateStudyRoomReservationStatus(@PathVariable Long reservationId, @RequestParam String status){
        AdminReservationResponse reservationResponse = reservationService.updateReservationStatus(reservationId, status);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<AdminReservationResponse>builder().result(reservationResponse).success(true).code(200).message("스터디룸 예약 상태 변경에 성공했습니다.").build());
    }
}
