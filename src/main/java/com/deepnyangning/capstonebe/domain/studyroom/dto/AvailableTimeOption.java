package com.deepnyangning.capstonebe.domain.studyroom.dto;

import lombok.*;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailableTimeOption {
    private LocalTime start;
    private List<LocalTime> end;
}
