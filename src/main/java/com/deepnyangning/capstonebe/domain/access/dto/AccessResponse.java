package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponse {
    private String identifier;
    private String name;
    private AuthMethod authMethod;
    private AccessType accessType;
    private Float similarity;
}