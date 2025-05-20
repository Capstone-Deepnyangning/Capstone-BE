package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.user.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessLogUserInfo {
    @Schema(description = "사용자 학번", example = "21011805")
    private String identifier;

    @Schema(description = "사용자 이름", example = "장윤정")
    private String name;

    @Schema(description = "사용자 권한", example = "USER")
    private Role role;

    @Schema(description = "활성 사용자 여부", example = "true")
    private boolean active;
}
