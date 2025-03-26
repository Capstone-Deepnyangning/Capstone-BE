package com.deepnyangning.capstonebe.global.response;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private int code;

    private String error;

    private String message;
}
