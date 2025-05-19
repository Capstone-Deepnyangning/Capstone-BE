package com.deepnyangning.capstonebe.domain.user.dto;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String identifier;

    private String name;

    private boolean faceRegistered;
}
