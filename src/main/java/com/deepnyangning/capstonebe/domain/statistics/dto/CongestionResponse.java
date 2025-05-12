package com.deepnyangning.capstonebe.domain.statistics.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CongestionResponse {
    private int currentUsers;

    private int avgUsers;
}
