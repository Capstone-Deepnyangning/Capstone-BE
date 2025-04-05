package com.deepnyangning.capstonebe.domain.user.dto;

import lombok.*;

@Getter
public class LoginRequest {
    private String identifier;
    private String password;
}
