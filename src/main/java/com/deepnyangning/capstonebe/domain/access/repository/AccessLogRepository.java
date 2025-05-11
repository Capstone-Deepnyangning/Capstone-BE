package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long>, JpaSpecificationExecutor<AccessLog> {
    int deleteByAccessTimeBefore(LocalDateTime before);

    Optional<AccessLog> findTopByUserAndAccessTypeOrderByAccessTimeDesc(User user, AccessType accessType);
}
