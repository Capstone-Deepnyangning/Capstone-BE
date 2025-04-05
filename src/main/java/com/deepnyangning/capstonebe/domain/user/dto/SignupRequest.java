package com.deepnyangning.capstonebe.domain.user.dto;

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
}
