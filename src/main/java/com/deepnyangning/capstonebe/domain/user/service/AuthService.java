package com.deepnyangning.capstonebe.domain.user.service;

import com.deepnyangning.capstonebe.domain.user.dto.SignupRequest;
import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.repository.UserRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request){
        if(userRepository.existsByIdentifier(request.getIdentifier())){
            throw new CustomException(ErrorCode.DUPLICATE_IDENTIFIER);
        }

        User user = User.builder()
                .identifier(request.getIdentifier())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .role(Role.USER)
                .isActive(true)
                .build();
        userRepository.save(user);
    }
}
