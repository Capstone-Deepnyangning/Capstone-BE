package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.dto.AccessResponse;
import com.deepnyangning.capstonebe.domain.access.dto.FaceAccessRequest;
import com.deepnyangning.capstonebe.domain.access.dto.QrAccessRequest;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.service.AccessService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/access")
public class AccessController {
    private final AccessService accessService;

    @PostMapping("/qr")
    public ResponseEntity<ApiResponse<Void>> processQrAccess(@Valid @RequestBody QrAccessRequest request){
        AccessResponse response = accessService.processQrAccess(request);
        String message = String.format("사용자 %s(%s)이/가 QR 코드 %s에 성공했습니다.",
                response.getName(),
                response.getIdentifier(),
                request.getAccessType() == AccessType.ENTRY ? "입장" : "퇴장");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message(message).build());
    }

    @PostMapping("/face")
    public ResponseEntity<ApiResponse<Void>> processFaceAccess(@Valid @RequestBody FaceAccessRequest request){
        AccessResponse response = accessService.processFaceAccess(request);
        String message = String.format("사용자 %s(%s)이/가 안면 인식 %s에 성공했습니다.",
                response.getName(),
                response.getIdentifier(),
                request.getAccessType() == AccessType.ENTRY ? "입장" : "퇴장");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message(message).build());
    }
}
