package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.FailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailLogRepository extends JpaRepository<FailLog, Long> {
}
