package com.deepnyangning.capstonebe.domain.user.dto;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String identifier;
    private String password;
    private String name;
    private String role;
}
