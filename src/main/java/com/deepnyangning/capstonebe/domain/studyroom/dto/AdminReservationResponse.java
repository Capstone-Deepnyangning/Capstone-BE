package com.deepnyangning.capstonebe.domain.studyroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminReservationResponse {
    private Long id;

    private ReservationUserInfo userInfo;

    private StudyRoomSimpleResponse studyRoom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    private String status;

    private String purpose;

    private List<ParticipantResponse> participants;
}
