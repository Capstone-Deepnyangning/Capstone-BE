package com.deepnyangning.capstonebe.domain.user.service;

import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.repository.UserRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findByIdentifier(String identifier){
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public User findById(Long id){
        return userRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public boolean existsByIdentifier(String identifier){
        return userRepository.existsByIdentifier(identifier);
    }

    public void saveUser(User user){
        userRepository.save(user);
    }

    public boolean existsByIdentifierAndName(String identifier, String name) {
        return userRepository.existsByIdentifierAndName(identifier, name);
    }
}
