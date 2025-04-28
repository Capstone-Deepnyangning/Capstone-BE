package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.dto.AccessRequest;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.service.AccessService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
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

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> processAccess(@RequestBody AccessRequest request){
        accessService.processAccess(request);
        String message = String.format("[%s] %s %s에 성공했습니다.",
                request.getIdentifier(),
                request.getAuthMethod() == AuthMethod.QR ? "QR 코드" : "안면 인식",
                request.getAccessType() == AccessType.ENTRY ? "입장" : "퇴장");
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message(message).build());
    }
}
