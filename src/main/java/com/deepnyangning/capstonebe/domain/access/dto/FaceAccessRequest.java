package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
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
    @NotBlank(message = "학번은 필수로 제공해야 합니다.")
    private String identifier;

    @NotNull(message = "출입 유형은 필수로 제공해야 합니다.")
    private AccessType accessType;

    @NotNull(message = "유사도는 필수로 제공해야 합니다.")
    @DecimalMin(value = "0.0", message = "유사도는 0.0 이상이어야 합니다.")
    @DecimalMax(value = "1.0", message = "유사도는 1.0 이하여야 합니다.")
    private Double similarity;
}
