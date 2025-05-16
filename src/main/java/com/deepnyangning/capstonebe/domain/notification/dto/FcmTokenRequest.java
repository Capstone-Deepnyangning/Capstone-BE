package com.deepnyangning.capstonebe.domain.notification.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FcmTokenRequest {
    private String token;
}
