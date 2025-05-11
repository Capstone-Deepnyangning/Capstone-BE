package com.deepnyangning.capstonebe.domain.statistics.repository;

import com.deepnyangning.capstonebe.domain.statistics.entity.GlobalAvg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface GlobalAvgRepository extends JpaRepository<GlobalAvg, Long> {
    Optional<GlobalAvg> findByWeekStartDate(LocalDate weekStartDate);
}
