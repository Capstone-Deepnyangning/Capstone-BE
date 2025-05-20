package com.deepnyangning.capstonebe.domain.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomResponse {
    @Schema(description = "스터디룸 ID", example = "3")
    private Long id;

    @Schema(description = "스터디룸 이름", example = "03 스터디룸(4층)")
    private String name;

    @Schema(description = "스터디룸 위치 정보", example = "학술정보원 4층")
    private String location;

    @Schema(description = "스터디룸 최소 수용 인원", example = "3")
    private int minCapacity;

    @Schema(description = "스터디룸 최대 수용 인원", example = "6")
    private int maxCapacity;

    @Schema(description = "예약 가능 시간 정보 (시간: 예약 가능 여부)",
            example = "{\"9\": true, \"10\": false, \"11\": true, \"12\": true, \"13\": false}")
    private Map<Integer, Boolean> reservedTimes;
}
