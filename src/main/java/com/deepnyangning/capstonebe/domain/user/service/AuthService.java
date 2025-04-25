package com.deepnyangning.capstonebe.domain.user.service;

import com.deepnyangning.capstonebe.domain.user.dto.CustomUserDetails;
import com.deepnyangning.capstonebe.domain.user.dto.LoginRequest;
import com.deepnyangning.capstonebe.domain.user.dto.LoginResponse;
import com.deepnyangning.capstonebe.domain.user.dto.SignupRequest;
import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import com.deepnyangning.capstonebe.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;

    private static final String REDIS_PREFIX = "RT:";


    public void signup(SignupRequest request){
        if(userService.existsByIdentifier(request.getIdentifier())){
            throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_ROLE);
        }

        User user = User.builder()
                .identifier(request.getIdentifier())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(role)
                .isActive(true)
                .build();
        userService.saveUser(user);
    }

    public LoginResponse login(LoginRequest request){
        try{
            // 인증 시도
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getIdentifier(), request.getPassword())
            );

            // 인증 성공 시 사용자 정보 가져오기
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 토큰 생성
            String accessToken = jwtUtil.createAccessToken(userDetails);
            String refreshToken = jwtUtil.createRefreshToken(userDetails);

            // Redis에 RefreshToken 저장
            tokenService.saveRefreshToken(userDetails.getUsername(), refreshToken);

            return new LoginResponse(accessToken, refreshToken);
        } catch (BadCredentialsException e){
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public LoginResponse reissue(String refreshToken){
        // 토큰 존재 검사
        if(refreshToken == null){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // Bearer 제거
        refreshToken = refreshToken.replace("Bearer ", "");

        // 토큰 유효성 검사
        if(!jwtUtil.isValid(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // RefreshToken 인지 검사
        if(!jwtUtil.getCategory(refreshToken).equals("refresh")){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        String identifier = jwtUtil.getSubject(refreshToken);

        // Redis에 저장된 리프레시 토큰과 비교
        String savedToken = tokenService.getRefreshToken(identifier);
        if(savedToken == null || !savedToken.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        // 새로운 토큰 생성
        User user = userService.findByIdentifier(identifier);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        String newAccessToken = jwtUtil.createAccessToken(customUserDetails);
        String newRefreshToken = jwtUtil.createRefreshToken(customUserDetails);

        // 기존 리프레시 토큰 삭제 후 새 토큰 저장
        tokenService.deleteRefreshToken(identifier);
        tokenService.saveRefreshToken(identifier, newRefreshToken);

        return new LoginResponse(newAccessToken, newRefreshToken);
    }

    public void logout(String identifier, String accessToken){
        // RefreshToken 삭제
        tokenService.deleteRefreshToken(identifier);

        // AccessToken 남은 만료 시간 추출 후 블랙리스트 처리
        accessToken = accessToken.replace("Bearer ", "");
        long expiration = jwtUtil.getRemainingTime(accessToken);
        tokenService.setBlacklist(accessToken, expiration);
    }
}
