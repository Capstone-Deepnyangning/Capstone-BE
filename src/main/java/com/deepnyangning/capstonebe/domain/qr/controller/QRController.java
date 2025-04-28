package com.deepnyangning.capstonebe.domain.qr.controller;

import com.deepnyangning.capstonebe.domain.qr.service.QRService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/qr")
public class QRController {
    private final QRService qrService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateQr(@RequestParam String identifier){
        String qrCode = qrService.generateQr(identifier);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<String>builder().result(qrCode).success(true).code(201).message("QR 코드가 성공적으로 생성되었습니다.").build());
    }
}
