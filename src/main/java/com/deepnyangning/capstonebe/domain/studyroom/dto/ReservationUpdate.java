package com.deepnyangning.capstonebe.domain.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdate {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "수정할 예약 날짜", example = "2025-05-20")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "수정할 예약 시작 시간", example = "13:00")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    @Schema(description = "수정할 예약 종료 시간", example = "15:00")
    private LocalTime endTime;

    @Schema(description = "수정할 예약 목적", example = "스터디")
    private String purpose;

    @Schema(description = "수정할 동반 이용자 리스트")
    private List<ParticipantRequest> participants;
}
