package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.dto.AccessRequest;
import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.access.repository.FailLogRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessService {
    private final AccessLogRepository accessLogRepository;
    private final FailLogRepository failLogRepository;
    private final UserService userService;

    @Transactional
    public void processAccess(AccessRequest request){
        User user;

        if(request.getAuthMethod() == AuthMethod.FACE){
            user = userService.findByIdentifier(request.getIdentifier());
            if(request.getSimilarity() == null){
                throw new CustomException(ErrorCode.MISSING_SIMILARITY);
            }
            if(request.getSimilarity() < 0.95) { // 임계값 추후 수정하기
                throw new CustomException(ErrorCode.INSUFFICIENT_SIMILARITY);
            }
        }
        else {
            user = userService.findByIdentifier(request.getIdentifier()); // qr 서비스 구현 후 수정하기
        }

        if(request.getAccessType() == AccessType.ENTRY){
            log.info("사용자 {}({})가 입장했습니다.", user.getIdentifier(), user.getName());
        }
        else{
            log.info("사용자 {}({})가 퇴장했습니다.", user.getIdentifier(), user.getName());
        }

        AccessLog accessLog = AccessLog.builder()
                .user(user)
                .authMethod(request.getAuthMethod())
                .accessType(request.getAccessType())
                .similarity(request.getSimilarity() == null ? 0.0f : request.getSimilarity())
                .build();
        accessLogRepository.save(accessLog);
    }
}
