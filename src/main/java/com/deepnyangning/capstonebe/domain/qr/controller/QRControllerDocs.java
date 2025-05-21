package com.deepnyangning.capstonebe.domain.qr.controller;

import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

public interface QRControllerDocs {
    @Operation(
            summary = "QR 코드 생성 API",
            description = """
                    **QR 코드 생성**
                                    
                    QR 코드를 생성합니다. \s
                    발급된 QR 코드는 3분동안 유효하며, 3분이 지나거나 이미 사용한 경우 재발급 받아야 합니다. \s
                    QR 코드를 통해 출입 인증을 요청할 수 있습니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                    
                         
                                                            
                    **응답**
                                    
                    - `ApiResponse<String>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "FCM 토큰 삭제에 성공했습니다."
                        - `result`: 생성된 QR 코드 문자열
                    """
    )
    public ResponseEntity<ApiResponse<String>> generateQr(@AuthenticationPrincipal UserDetails userDetails);
}
