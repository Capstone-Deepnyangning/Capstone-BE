package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantResponse {
    private Long id;

    private String identifier;

    private String name;
}
