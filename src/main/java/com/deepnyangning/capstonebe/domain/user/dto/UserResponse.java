package com.deepnyangning.capstonebe.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @Schema(description = "사용자 학번", example = "21011805")
    private String identifier;

    @Schema(description = "사용자 이름", example = "장윤정")
    private String name;

    @Schema(description = "얼굴 인식 등록 여부", example = "true")
    private boolean faceRegistered;
}
