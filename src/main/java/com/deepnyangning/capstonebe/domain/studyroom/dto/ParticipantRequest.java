package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequest {
    private String identifier;

    private String name;
}
