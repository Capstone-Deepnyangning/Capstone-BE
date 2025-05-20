package com.deepnyangning.capstonebe.domain.user.controller;

import com.deepnyangning.capstonebe.domain.user.dto.CustomUserDetails;
import com.deepnyangning.capstonebe.domain.user.dto.LoginRequest;
import com.deepnyangning.capstonebe.domain.user.dto.LoginResponse;
import com.deepnyangning.capstonebe.domain.user.dto.SignupRequest;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthControllerDocs {
    @Operation(
            summary = "회원가입 API",
            description = """
                     **회원가입 처리**
                                               
                     사용자가 계정을 생성합니다. \s
                     학번(identifier), 비밀번호, 이름 정보를 입력하고, 역할 정보를 포함하여 요청합니다.
                     
                     **처리 흐름**
                     - 학번 중복 여부 확인
                     - 입력된 역할(Role)이 유효한 Enum 값인지 확인
                     - 비밀번호 암호화 후 사용자 저장
                     
                     **예외 사항**
                     - 학번이 이미 존재하는 경우 예외 발생
                     - 역할이 USER, ADMIN 등의 Enum에 맞지 않는 경우 예외 발생
                          
                                         
                         
                     **요청 필드**
                     - `identifier` : 학번 (예: "21011805")
                     - `password` : 비밀번호 (예: "password")
                     - `name` : 이름 (예: "장윤정")
                     - `role` : 역할 (예: "USER")
                             
                             
                                        
                     **응답**
                     - `ApiResponse<Void>`
                       - `success`: true
                       - `code`: HTTP 상태 코드 (201)
                       - `message`: "회원가입에 성공했습니다."
                     """
    )
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request);

    @Operation(
            summary = "로그인 API",
            description = """
                    **로그인 처리**

                    사용자가 학번과 비밀번호로 로그인하면 AccessToken과 RefreshToken을 발급합니다.
                                        
                    **처리 흐름**
                    - AuthenticationManager를 통해 로그인 인증 수행
                    - 인증 성공 시 AccessToken 및 RefreshToken 생성
                    - RefreshToken은 Redis에 저장
                                        
                    **예외 사항**
                    - 비밀번호 불일치 시 로그인 실패 처리
                    - 학번이 존재하지 않거나 비활성화된 경우 예외 발생
                                        
                    **요청 필드**
                    - `identifier` : 학번 (예: "21011805")
                    - `password` : 비밀번호 (예: "password")
                       
                       
                                        
                    **응답**
                    - `ApiResponse<LoginResponse>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "로그인에 성공했습니다."
                      - `result`
                        - `accessToken` : 액세스 토큰 문자열 (예: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                        - `refreshToken` : 리프레시 토큰 문자열 (예: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
                    """
    )
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request);

    @Operation(
            summary = "토큰 재발급 API",
            description = """
                    **AccessToken 및 RefreshToken 재발급**

                    사용자가 RefreshToken을 통해 새로운 AccessToken과 RefreshToken을 발급받습니다. \s
                    이를 통해 AccessToken이 만료된 경우에도 재로그인 과정 없이 인증 정보를 유지할 수 있습니다.
                    
                    **처리 흐름**
                    - Request Attribute에서 RefreshToken 추출
                    - 토큰 유효성 및 타입(refresh) 검증
                    - Redis에 저장된 토큰과 일치 여부 확인
                    - 일치하면 새로운 토큰 발급 후 Redis 갱신
                    
                    **예외 사항**
                    - RefreshToken이 없거나 유효하지 않은 경우
                    - Redis에 저장된 토큰과 일치하지 않는 경우
                    
                    **요청 헤더**
                                    
                    - `Authorization: Bearer {refreshToken}` : accessToken 대신 refreshToken을 헤더에 포함해야 합니다.
                    
                    
                    
                    **응답**
                    - `ApiResponse<LoginResponse>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "토큰 재발급에 성공했습니다."
                      - `result`
                        - `accessToken` : 새롭게 발급된 액세스 토큰 (예: "newAccessToken")
                        - `refreshToken` : 새롭게 발급된 리프레시 토큰 (예: "newRefreshToken")
                    """
    )
    public ResponseEntity<ApiResponse<LoginResponse>> reissue(HttpServletRequest request);

    @Operation(
            summary = "로그아웃 API",
            description = """
                    **로그아웃 처리**

                    사용자가 로그아웃 요청 시, RefreshToken 삭제 및 AccessToken을 블랙리스트 처리합니다.
                    
                    **처리 흐름**
                    - 사용자 정보(@AuthenticationPrincipal)로 식별자 추출
                    - Redis에서 RefreshToken 삭제
                    - AccessToken의 남은 유효 시간 추출 후 블랙리스트 처리
                    
                    **예외 사항**
                    - AccessToken이 비정상적인 경우 추후 인증 시도 시 거부됨
                    
                    **요청 헤더**
                    - `Authorization: Bearer {accessToken}` : 사용자 인증을 위해 accessToken을 헤더에 포함해야 합니다.
                    
                    
                    
                    **응답**
                    - `ApiResponse<Void>`
                      - `success`: true
                      - `code`: HTTP 상태 코드 (200)
                      - `message`: "로그아웃에 성공했습니다."
                    """
    )
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    HttpServletRequest request);
}
