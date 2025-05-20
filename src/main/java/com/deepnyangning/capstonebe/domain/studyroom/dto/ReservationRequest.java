package com.deepnyangning.capstonebe.domain.studyroom.dto;

import com.deepnyangning.capstonebe.domain.studyroom.entity.ReservationStatus;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoom;
import com.deepnyangning.capstonebe.domain.studyroom.entity.StudyRoomParticipant;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    @Schema(description = "예약할 스터디룸 ID", example = "3")
    private Long studyRoomId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "예약 날짜", example = "2025-05-20")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "예약 시작 시간", example = "13:00")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "예약 종료 시간", example = "15:00")
    private LocalTime endTime;

    @Schema(description = "예약 목적", example = "스터디")
    private String purpose;

    @Schema(description = "동반 이용자 리스트")
    private List<ParticipantRequest> participants;
}
