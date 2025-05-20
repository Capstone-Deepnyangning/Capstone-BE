package com.deepnyangning.capstonebe.domain.access.dto;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QrAccessRequest {
    @Schema(description = "생성된 QR 코드 문자열", example = "QRCODE-1234567890")
    @NotBlank(message = "QR Code는 필수로 제공해야 합니다.")
    private String qrCode;

    @Schema(description = "출입 타입 (ENTRY 또는 EXIT)", example = "ENTRY")
    @NotNull(message = "출입 유형은 필수로 제공해야 합니다.")
    private AccessType accessType;
}
