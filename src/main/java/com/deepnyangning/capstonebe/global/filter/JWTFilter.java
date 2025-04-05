package com.deepnyangning.capstonebe.global.filter;

import com.deepnyangning.capstonebe.domain.user.dto.CustomUserDetails;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.TokenService;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final UserService userService;

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
            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // response body
            PrintWriter writer = response.getWriter();
            writer.print("token expired");
            writer.flush();
            return;
        }

        // 블랙리스트 여부 확인
        if(tokenService.isBlacklisted(token)){
            // response body
            PrintWriter writer = response.getWriter();
            writer.print("token blacklisted");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 사용자 정보 추출
        String identifier = jwtUtil.getSubject(token);
        User user = userService.findByIdentifier(identifier);
        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContext에 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 넘기기
        filterChain.doFilter(request, response);
    }
}
