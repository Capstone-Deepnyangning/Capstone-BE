package com.deepnyangning.capstonebe.domain.statistics.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CongestionResponse {
    @Schema(description = "현재 도서관 이용자 수", example = "503")
    private int currentUsers;

    @Schema(description = "최근 일주일 평균 도서관 이용자 수", example = "452")
    private int avgUsers;
}
