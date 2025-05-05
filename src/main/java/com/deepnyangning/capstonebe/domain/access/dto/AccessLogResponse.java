package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogResponse {
    private Long id;

    private AccessLogUserInfo userInfo;

    private AccessType accessType;

    private AuthMethod authMethod;

    private LocalDateTime accessTime;

    private float similarity;
}
