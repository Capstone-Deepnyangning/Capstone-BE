package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long>, JpaSpecificationExecutor<AccessLog> {
    int deleteByAccessTimeBefore(LocalDateTime before);
}
