package com.deepnyangning.capstonebe.domain.studyroom.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomSimpleResponse {
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
}
