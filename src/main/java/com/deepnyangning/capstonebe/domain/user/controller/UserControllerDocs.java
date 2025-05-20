package com.deepnyangning.capstonebe.domain.user.controller;

import com.deepnyangning.capstonebe.domain.user.dto.PasswordUpdate;
import com.deepnyangning.capstonebe.domain.user.dto.UserResponse;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserControllerDocs {
    @Operation(
            summary = "마이페이지 조회 API",
            description = """
                    **마이페이지 조회**

                    현재 로그인된 사용자의 정보를 조회합니다.
                    
                    **처리 흐름**
                    - AuthenticationPrincipal로 사용자 식별
                    - 해당 학번으로 사용자 조회
                    - 사용자 정보 응답 반환 (학번, 이름, 얼굴 등록 여부)
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                    
                    
                    
                    **응답**
                    - `ApiResponse<UserResponse>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "마이페이지 조회에 성공했습니다."
                      - `result`
                        - `identifier`: 사용자 학번 (예: "21011805")
                        - `name`: 사용자 이름 (예: "장윤정")
                        - `faceRegistered`: 얼굴 등록 여부 (예: true)
                    """
    )
    public ResponseEntity<ApiResponse<UserResponse>> getMyPage(@AuthenticationPrincipal UserDetails userDetails);

    @Operation(
            summary = "비밀번호 수정 API",
            description = """
                    **비밀번호 수정**

                    사용자가 현재 비밀번호를 검증한 후 새 비밀번호로 변경합니다.
                                        
                    **처리 흐름**
                    - 현재 비밀번호가 올바른지 확인
                    - 새 비밀번호가 기존 비밀번호와 동일하면 예외 발생
                    - 새 비밀번호로 암호화 후 저장
                    
                    **예외 사항**
                    - 현재 비밀번호가 일치하지 않으면 예외 발생
                    - 새 비밀번호가 기존 비밀번호와 동일하면 예외 발생
                                        
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                                        
                    **요청 필드**
                    - `currentPassword` : 현재 비밀번호 (예: "oldpassword123")
                    - `newPassword` : 새 비밀번호 (예: "newpassword456")                    
                                        
                    **응답**
                    - `ApiResponse<Void>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "비밀번호 변경에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordUpdate passwordUpdate);

    @Operation(
            summary = "회원 탈퇴 API",
            description = """
                    **회원 탈퇴**

                    현재 로그인된 사용자의 계정을 비활성화 처리합니다.
                                        
                    **처리 흐름**
                    - 로그인된 사용자 식별
                    - 해당 사용자 엔티티의 active 값을 false로 설정
                                        
                    **예외 사항**
                    - 사용자가 존재하지 않는 경우 예외 발생
                                        
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                                        
                    **요청 필드**
                    - `currentPassword` : 현재 비밀번호 (예: "oldpassword123")
                    - `newPassword` : 새 비밀번호 (예: "newpassword456")                    
                                        
                    **응답**
                    - `ApiResponse<Void>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "회원 탈퇴에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal UserDetails userDetails);
}
