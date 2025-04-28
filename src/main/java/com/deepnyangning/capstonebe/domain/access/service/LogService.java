package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.access.repository.FailLogRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final AccessLogRepository accessLogRepository;
    private final FailLogRepository failLogRepository;

    @Transactional
    public void saveAccessLog(User user, AuthMethod authMethod, AccessType accessType, Float similarity){
        AccessLog accessLog = AccessLog.builder()
                .user(user)
                .authMethod(authMethod)
                .accessType(accessType)
                .similarity(similarity == null ? 0.0f : similarity)
                .build();
        accessLogRepository.save(accessLog);

        String message = String.format("사용자 %s(%s)이/가 %s했습니다.",
                user.getName(), user.getIdentifier(), accessType == AccessType.ENTRY ? "입장" : "퇴장");
        log.info(message);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailLog(AuthMethod authMethod, Float similarity, ErrorCode errorCode){
        FailLog failLog = FailLog.builder()
                .authMethod(authMethod)
                .similarity(similarity)
                .build();
        failLogRepository.saveAndFlush(failLog);

        String message = String.format("출입 인증 실패 - authMethod: %s, error: %s",
                authMethod, errorCode.name());
        log.warn(message);
    }
}
