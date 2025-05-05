package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.access.repository.FailLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogCleanupService {
    private final AccessLogRepository accessLogRepository;
    private final FailLogRepository failLogRepository;

    @Transactional
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시에 실행
    public void cleanOldLogs() {
        LocalDateTime before = LocalDateTime.now().minusDays(7);
        int deletedAccess = accessLogRepository.deleteByAccessTimeBefore(before);
        int deletedFail = failLogRepository.deleteByAccessTimeBefore(before);

        log.info("로그 정리 완료: AccessLog {}건, FailLog {}건이 삭제되었습니다.", deletedAccess, deletedFail);
    }
}
