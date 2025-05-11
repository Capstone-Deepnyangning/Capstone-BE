package com.deepnyangning.capstonebe.domain.statistics.repository;

import com.deepnyangning.capstonebe.domain.statistics.entity.DailyStay;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyStayRepository extends JpaRepository<DailyStay, Long> {
    List<DailyStay> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

    Optional<DailyStay> findByUserAndDate(User user, LocalDate date);
}
