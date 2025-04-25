package com.deepnyangning.capstonebe.domain.user.controller;

import com.deepnyangning.capstonebe.domain.user.dto.CustomUserDetails;
import com.deepnyangning.capstonebe.domain.user.dto.LoginRequest;
import com.deepnyangning.capstonebe.domain.user.dto.LoginResponse;
import com.deepnyangning.capstonebe.domain.user.dto.SignupRequest;
import com.deepnyangning.capstonebe.domain.user.service.AuthService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest request){
        authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Void>builder().success(true).code(201).message("회원가입에 성공했습니다.").build());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request){
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok()
                .body(ApiResponse.<LoginResponse>builder().result(response).success(true).code(200).message("로그인에 성공했습니다.").build());
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<LoginResponse>> reissue(HttpServletRequest request){
        String refreshToken = (String) request.getAttribute("refreshToken");
        LoginResponse response = authService.reissue(refreshToken);
        return ResponseEntity.ok()
                .body(ApiResponse.<LoginResponse>builder().result(response).success(true).code(200).message("토큰이 성공적으로 재발급되었습니다.").build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    HttpServletRequest request){
        String accessToken = (String) request.getAttribute("accessToken");
        authService.logout(userDetails.getUsername(), accessToken);
        return ResponseEntity.ok().body(ApiResponse.<Void>builder().success(true).code(200).message("로그아웃에 성공했습니다.").build());
    }
}
