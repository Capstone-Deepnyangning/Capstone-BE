package com.deepnyangning.capstonebe.domain.access.repository;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long>, JpaSpecificationExecutor<AccessLog> {
    int deleteByAccessTimeBefore(LocalDateTime before);

    Optional<AccessLog> findTopByUserAndAccessTypeOrderByAccessTimeDesc(User user, AccessType accessType);

    @Query("SELECT COUNT(DISTINCT a.user.id) " +
            "FROM AccessLog a " +
            "WHERE a.accessType = 'ENTRY' " +
            "AND NOT EXISTS (" +
            "SELECT 1 FROM AccessLog e " +
            "WHERE e.user.id = a.user.id " +
            "AND e.accessType = 'EXIT' " +
            "AND e.accessTime > a.accessTime)")
    int countCurrentUsers();

  @Query("SELECT CAST(a.accessTime AS date) as date, COUNT(DISTINCT a.user.id) as userCnt " +
         "FROM AccessLog a " +
         "WHERE a.accessType = 'ENTRY' " +
         "AND a.accessTime >= :startDate " +
         "AND a.accessTime < :endDate " +
         "GROUP BY CAST(a.accessTime AS date)")
  List<Object[]> countDailyUsers(@Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);
}
