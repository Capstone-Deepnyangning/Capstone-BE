package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FailLogRepository extends JpaRepository<FailLog, Long>, JpaSpecificationExecutor<FailLog> {
    int deleteByAccessTimeBefore(LocalDateTime before);
}
