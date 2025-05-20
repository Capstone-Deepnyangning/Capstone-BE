package com.deepnyangning.capstonebe.domain.notification.controller;

import com.deepnyangning.capstonebe.domain.notification.dto.FcmTokenRequest;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Tag(name = "푸시 알림 API")
public class NotificationController implements NotificationControllerDocs {
    private final NotificationService notificationService;

    @PostMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> saveFcmToken(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody FcmTokenRequest request){
        String identifier = userDetails.getUsername();
        notificationService.saveToken(identifier, request.getToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message("FCM 토큰 저장에 성공했습니다.").build());
    }

    @DeleteMapping("/tokens")
    public ResponseEntity<ApiResponse<Void>> deleteFcmToken(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody FcmTokenRequest request){
        String identifier = userDetails.getUsername();
        notificationService.deleteToken(identifier, request.getToken());
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message("FCM 토큰 삭제에 성공했습니다.").build());
    }
}
