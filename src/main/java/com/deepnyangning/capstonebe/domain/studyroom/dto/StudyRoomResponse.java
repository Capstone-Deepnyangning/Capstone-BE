package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomResponse {
    private Long id;

    private String name;

    private int minCapacity;

    private int maxCapacity;
}
