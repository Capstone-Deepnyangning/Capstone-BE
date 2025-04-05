package com.deepnyangning.capstonebe.domain.user.dto;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
}
