package com.deepnyangning.capstonebe.domain.user.controller;

import com.deepnyangning.capstonebe.domain.user.dto.PasswordUpdate;
import com.deepnyangning.capstonebe.domain.user.dto.UserResponse;
import com.deepnyangning.capstonebe.domain.user.mapper.UserMapper;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "마이페이지 API")
public class UserController implements UserControllerDocs {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<UserResponse>> getMyPage(@AuthenticationPrincipal UserDetails userDetails){
        String identifier = userDetails.getUsername();
        UserResponse userResponse = userMapper.toResponseDto(userService.findByIdentifier(identifier));
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<UserResponse>builder().result(userResponse).success(true).code(200).message("회원 정보 조회에 성공했습니다.").build());
    }

    @PatchMapping("/password")
    public ResponseEntity<ApiResponse<Void>> updatePassword(@AuthenticationPrincipal UserDetails userDetails, @Valid @RequestBody PasswordUpdate passwordUpdate){
        String identifier = userDetails.getUsername();
        userService.updatePassword(identifier, passwordUpdate);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message("비밀번호를 성공적으로 변경했습니다.").build());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteUser(@AuthenticationPrincipal UserDetails userDetails){
        String identifier = userDetails.getUsername();
        userService.deleteUser(identifier);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.<Void>builder().success(true).code(200).message("회원 탈퇴에 성공했습니다.").build());
    }
}
