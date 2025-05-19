package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudyRoomSimpleResponse {
    private Long id;

    private String name;

    private String location;

    private int minCapacity;

    private int maxCapacity;
}
