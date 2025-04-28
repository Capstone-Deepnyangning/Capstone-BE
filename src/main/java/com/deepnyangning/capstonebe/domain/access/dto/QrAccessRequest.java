package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrAccessRequest {
    @NotBlank(message = "QR Code는 필수로 제공해야 합니다.")
    private String qrCode;

    @NotNull(message = "출입 유형은 필수로 제공해야 합니다.")
    private AccessType accessType;
}
