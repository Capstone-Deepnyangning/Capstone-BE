package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.dto.AccessLogPreviewResponse;
import com.deepnyangning.capstonebe.domain.access.dto.AccessLogResponse;
import com.deepnyangning.capstonebe.domain.access.dto.FailLogResponse;
import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import com.deepnyangning.capstonebe.domain.access.mapper.AccessLogMapper;
import com.deepnyangning.capstonebe.domain.access.mapper.FailLogMapper;
import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.access.repository.FailLogRepository;
import com.deepnyangning.capstonebe.domain.access.specification.AccessLogSpecification;
import com.deepnyangning.capstonebe.domain.access.specification.FailLogSpecification;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.mapper.UserMapper;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogService {
    private final AccessLogRepository accessLogRepository;
    private final FailLogRepository failLogRepository;
    private final AccessLogMapper accessLogMapper;
    private final FailLogMapper failLogMapper;

    @Transactional
    public void saveAccessLog(User user, AuthMethod authMethod, AccessType accessType, Double similarity){
        AccessLog accessLog = AccessLog.builder()
                .user(user)
                .authMethod(authMethod)
                .accessType(accessType)
                .similarity(similarity == null ? 0.0 : similarity)
                .build();
        accessLogRepository.save(accessLog);

        String message = String.format("사용자 %s(%s)이/가 %s했습니다.",
                user.getName(), user.getIdentifier(), accessType == AccessType.ENTRY ? "입장" : "퇴장");
        log.info(message);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailLog(AuthMethod authMethod, Double similarity, ErrorCode errorCode){
        FailLog failLog = FailLog.builder()
                .authMethod(authMethod)
                .similarity(similarity)
                .build();
        failLogRepository.saveAndFlush(failLog);

        String message = String.format("출입 인증 실패 - authMethod: %s, error: %s",
                authMethod, errorCode.name());
        log.warn(message);
    }

    public Page<AccessLogPreviewResponse> findAccessLogs(LocalDateTime startTime, LocalDateTime endTime,
                                                         String identifier, String name, AuthMethod authMethod, Pageable pageable){
        Specification<AccessLog> spec = AccessLogSpecification.withFilters(startTime, endTime, identifier, name, authMethod);
        return accessLogRepository.findAll(spec, pageable).map(accessLogMapper::toPreviewDto);
    }

    public AccessLogResponse findAccessLog(Long logId){
        return accessLogMapper.toResponseDto(accessLogRepository.findById(logId)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_LOG_NOT_FOUND)));
    }

    public Page<FailLogResponse> findFailLogs(LocalDateTime startTime, LocalDateTime endTime, AuthMethod authMethod, Pageable pageable){
        Specification<FailLog> spec = FailLogSpecification.withFilters(startTime, endTime, authMethod);
        return failLogRepository.findAll(spec, pageable).map(failLogMapper::toResponseDto);
    }

    public LocalDateTime findLatestEntryTime(User user){
        return accessLogRepository.findTopByUserAndAccessTypeOrderByAccessTimeDesc(user, AccessType.ENTRY)
                .orElseThrow(() -> new CustomException(ErrorCode.ACCESS_LOG_NOT_FOUND)).getAccessTime();
    }
}
