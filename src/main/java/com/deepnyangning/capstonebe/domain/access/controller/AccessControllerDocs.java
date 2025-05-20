package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.dto.FaceAccessRequest;
import com.deepnyangning.capstonebe.domain.access.dto.QrAccessRequest;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccessControllerDocs {
    @Operation(
            summary = "QR 출입 인증 요청 API",
            description = """
                    **QR 출입 인증 요청**
                                    
                    QR을 통해 출입 인증을 요청합니다. \s
                    QR 코드가 Redis에 저장되어 있고 생성 후 3분 이내일 경우 유효한 것으로 간주되어 출입이 승인되며, 출입 로그가 기록됩니다.
                                    
                    **요청 필드**
                                    
                    - `String qrCode` : 생성된 QR 코드
                    - `AccessType accessType` : 출입 타입 ("ENTRY" or "EXIT")
                        
                                    
                                    
                    **응답**
                                    
                    - `ApiResponse<Void>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (200)
                        - `message`: "사용자 장윤정(21011805)이/가 QR 코드 입장에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> processQrAccess(@Valid @RequestBody QrAccessRequest request);

    @Operation(
            summary = "안면 인식 출입 인증 요청 API",
            description = """
                **안면 인식 출입 인증 요청**
                
                AI 서버에서 안면 인식이 완료되면 호출되어 출입 인증을 수행합니다. \s
                학번이 유효하고 유사도가 0.95 이상일 경우 출입이 승인되며, 출입 로그가 기록됩니다.
                
                **요청 필드**
                
                - `String identifier` : 사용자 학번
                - `AccessType accessType` : 출입 타입 ("ENTRY" or "EXIT")
                - `Double similarity` : 유사도 (예: 0.98)
                    
                
                
                **응답**
                
                - `ApiResponse<Void>`
                    - `success`: true
                    - `code`: HTTP 상태 코드 (200)
                    - `message`: "사용자 장윤정(21011805)이/가 안면 인식 입장에 성공했습니다."           
                """
    )
    public ResponseEntity<ApiResponse<Void>> processFaceAccess(@Valid @RequestBody FaceAccessRequest request);
}
