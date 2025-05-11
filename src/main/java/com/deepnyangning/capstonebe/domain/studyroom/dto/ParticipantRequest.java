package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipantRequest {
    private String identifier;

    private String name;

    private LocalDate date;
}
