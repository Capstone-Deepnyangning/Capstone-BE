package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FaceAccessRequest {
    @Schema(description = "사용자 학번", example = "21011805")
    @NotBlank(message = "학번은 필수로 제공해야 합니다.")
    private String identifier;

    @Schema(description = "출입 타입 (ENTRY 또는 EXIT)", example = "EXIT")
    @NotNull(message = "출입 유형은 필수로 제공해야 합니다.")
    private AccessType accessType;

    @Schema(description = "AI가 계산한 안면 유사도 (0.0 ~ 1.0)", example = "0.98")
    @NotNull(message = "유사도는 필수로 제공해야 합니다.")
    @DecimalMin(value = "0.0", message = "유사도는 0.0 이상이어야 합니다.")
    @DecimalMax(value = "1.0", message = "유사도는 1.0 이하여야 합니다.")
    private Double similarity;
}
