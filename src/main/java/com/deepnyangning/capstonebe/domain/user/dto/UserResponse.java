package com.deepnyangning.capstonebe.domain.user.dto;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;

    private String identifier;

    private String name;

    private Role role;

    private boolean active;
}
