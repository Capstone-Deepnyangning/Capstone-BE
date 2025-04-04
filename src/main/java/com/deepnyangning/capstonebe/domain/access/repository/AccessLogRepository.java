package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
}
