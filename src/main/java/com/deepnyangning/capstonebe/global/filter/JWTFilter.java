package com.deepnyangning.capstonebe.global.filter;

import com.deepnyangning.capstonebe.domain.user.service.TokenService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.response.ErrorResponse;
import com.deepnyangning.capstonebe.global.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 요청 헤더에서 Authorization 추출
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.replace("Bearer ", "");

        // 요청 URI 확인
        String uri = request.getRequestURI();

        if (uri.equals("/auth/reissue")) {
            request.setAttribute("refreshToken", token);
        } else {
            request.setAttribute("accessToken", token);
        }

        // JWT 유효성 검사
        try {
            jwtUtil.isValid(token);
        } catch (ExpiredJwtException e) {
            // error response 설정
            setErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
            return;
        }

        // 블랙리스트 여부 확인
        if(tokenService.isBlacklisted(token)){
            // error response 설정
            setErrorResponse(response, ErrorCode.TOKEN_ALREADY_LOGOUT);
            return;
        }

        // 사용자 정보 추출
        String identifier = jwtUtil.getSubject(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(identifier);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContext에 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }

    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .error(errorCode.getHttpStatus().name())
                .message(errorCode.getMessage())
                .build();

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
