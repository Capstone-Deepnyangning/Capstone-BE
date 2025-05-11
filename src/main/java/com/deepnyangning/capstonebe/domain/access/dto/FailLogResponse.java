package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FailLogResponse {
    private Long id;

    private AuthMethod authMethod;

    private LocalDateTime accessTime;

    private Double similarity;
}
