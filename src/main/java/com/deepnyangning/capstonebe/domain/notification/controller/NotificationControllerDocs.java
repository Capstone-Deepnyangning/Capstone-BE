package com.deepnyangning.capstonebe.domain.notification.controller;

import com.deepnyangning.capstonebe.domain.notification.dto.FcmTokenRequest;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

public interface NotificationControllerDocs {
    @Operation(
            summary = "FCM 토큰 저장 API",
            description = """
                    **사용자의 FCM 토큰 저장**
                                    
                    클라이언트에서 발급받은 FCM 푸시 알림 토큰을 서버에 저장합니다. \s
                    저장된 토큰은 추후 푸시 알림 전송 시 사용됩니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                    
                         
                                    
                    **요청 필드**
                                    
                    - `token` : FCM 토큰 문자열
                                    
                                    
                                                
                    **응답**
                                    
                    - `ApiResponse<Void>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "FCM 토큰 저장에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> saveFcmToken(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestBody FcmTokenRequest request);

    @Operation(
            summary = "FCM 토큰 삭제 API",
            description = """
                    **사용자의 FCM 토큰 삭제**
                                    
                    저장된 FCM 푸시 알림 토큰을 삭제합니다. \s
                    클라이언트가 푸시 알림 수신을 원치 않거나, 로그아웃 등의 상황에서 호출합니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                    
                         
                                    
                    **요청 필드**
                                    
                    - `token` : 삭제할 FCM 토큰 문자열
                                    
                                    
                                                
                    **응답**
                                    
                    - `ApiResponse<Void>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "FCM 토큰 삭제에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> deleteFcmToken(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody FcmTokenRequest request);
}
