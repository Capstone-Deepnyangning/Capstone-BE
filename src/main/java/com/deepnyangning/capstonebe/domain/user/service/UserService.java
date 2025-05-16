package com.deepnyangning.capstonebe.domain.user.service;

import com.deepnyangning.capstonebe.domain.user.dto.PasswordUpdate;
import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.repository.UserRepository;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User findByIdentifier(String identifier){
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public List<User> findAdmins(){
        return userRepository.findByRole(Role.ADMIN);
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

    public void verifyCurrentPassword(String identifier, String currentPassword){
        User user = findByIdentifier(identifier);
        if(!passwordEncoder.matches(currentPassword, user.getPassword())){
            throw new CustomException(ErrorCode.INCORRECT_CURRENT_PASSWORD);
        }
    }

    @Transactional
    public void updatePassword(String identifier, PasswordUpdate passwordUpdate){
        User user = findByIdentifier(identifier);
        verifyCurrentPassword(identifier, passwordUpdate.getCurrentPassword());
        if(passwordEncoder.matches(passwordUpdate.getNewPassword(), user.getPassword())){
            throw new CustomException(ErrorCode.SAME_AS_OLD_PASSWORD);
        }
        user.setPassword(passwordEncoder.encode(passwordUpdate.getNewPassword()));
    }

    @Transactional
    public void deleteUser(String identifier){
        User user = findByIdentifier(identifier);
        user.setActive(false);
    }
}
