package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface FaceDataControllerDocs {
    @Operation(
            summary = "얼굴 등록 API",
            description = """
                    **사용자의 얼굴 데이터 등록**
                                    
                    사용자가 얼굴 영상을 업로드하여 안면 데이터를 등록합니다. \s
                    업로드된 영상은 사용자 인증 후 학번과 함께 AI 서버로 전송되며, 얼굴 벡터값이 추출됩니다. \s
                    등록된 얼굴 데이터는 이후 출입 인증 시 사용됩니다.
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자의 인증 정보를 추출하기 위해 accessToken을 헤더에 포함해야 합니다.
                    
                         
                                    
                    **요청 필드**
                                    
                    - `MultipartFile file` : 얼굴 영상 파일 (.mp4)
                                    
                                    
                                                
                    **응답**
                                    
                    - `ApiResponse<Void>`
                        - `success`: true
                        - `code`: HTTP 상태 코드 (201)
                        - `message`: "얼굴 등록에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> registerFace(@AuthenticationPrincipal UserDetails userDetails,
                                                          @RequestParam("file") MultipartFile file);
}
