package com.deepnyangning.capstonebe.domain.statistics.service;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.service.LogService;
import com.deepnyangning.capstonebe.domain.statistics.dto.DailyStayResponse;
import com.deepnyangning.capstonebe.domain.statistics.dto.MonthlyStatResponse;
import com.deepnyangning.capstonebe.domain.statistics.dto.WeeklyStatResponse;
import com.deepnyangning.capstonebe.domain.statistics.entity.DailyStay;
import com.deepnyangning.capstonebe.domain.statistics.entity.GlobalAvg;
import com.deepnyangning.capstonebe.domain.statistics.mapper.DailyStayMapper;
import com.deepnyangning.capstonebe.domain.statistics.repository.DailyStayRepository;
import com.deepnyangning.capstonebe.domain.statistics.repository.GlobalAvgRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final DailyStayRepository dailyStayRepository;
    private final GlobalAvgRepository globalAvgRepository;
    private final UserService userService;
    private final DailyStayMapper dailyStayMapper;
    private final LogService logService;

    // 개인별 weekly 출입 통계
    public WeeklyStatResponse getWeeklyStatistics(String identifier){
        User user = userService.findByIdentifier(identifier);

        LocalDate startDate = LocalDate.now().with(DayOfWeek.MONDAY);
        LocalDate endDate = LocalDate.now().with(DayOfWeek.SUNDAY);

        List<DailyStayResponse> weeklyStays = dailyStayRepository.findByUserAndDateBetween(user, startDate, endDate)
                .stream().map(dailyStayMapper::toResponseDto).toList();
        float weeklyAvg = (float) weeklyStays.stream()
                .mapToInt(DailyStayResponse::getStayMinutes)
                .sum() / 7f;
        double globalAvg = getGlobalStayAvg(startDate);

        return WeeklyStatResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .weeklyAvg(weeklyAvg)
                .globalAvg(globalAvg)
                .weeklyStay(weeklyStays)
                .build();
    }

    public double getGlobalStayAvg(LocalDate weekStartDate){
        return globalAvgRepository.findByWeekStartDate(weekStartDate)
                .orElseThrow(() -> new CustomException(ErrorCode.GLOBAL_AVG_NOT_FOUND))
                .getGlobalAvg();
    }

    // 개인별 monthly 출입 통계
    public MonthlyStatResponse getMonthlyStatistics(String identifier, int year, int month) {
        User user = userService.findByIdentifier(identifier);

        YearMonth yearMonth = YearMonth.of(year, month);
        List<DailyStayResponse> monthlyStay = dailyStayRepository.findByUserAndDateBetween(user, yearMonth.atDay(1), yearMonth.atEndOfMonth())
                .stream().map(dailyStayMapper::toResponseDto).toList();
        int totalStay = monthlyStay.stream()
                .mapToInt(DailyStayResponse::getStayMinutes)
                .sum();

        return MonthlyStatResponse.builder()
                .year(year)
                .month(month)
                .totalStay(totalStay)
                .monthlyStay(monthlyStay)
                .build();
    }

    // 퇴장 시 개인별 daily 통계 + global 통계 반영
    @Transactional
    public void updateDailyStay(User user, AccessType accessType){
        if (accessType != AccessType.EXIT) return;

        LocalDateTime entryAt = logService.findLatestEntryTime(user);
        LocalDate today = LocalDate.now();
        if(!entryAt.toLocalDate().equals(today)) return;

        long stayedMinutes = Duration.between(entryAt, LocalDateTime.now()).toMinutes();

        // 개인 daily 통계 업데이트
        DailyStay dailyStay = dailyStayRepository.findByUserAndDate(user, today)
                .orElse(DailyStay.builder()
                        .user(user)
                        .date(today)
                        .totalStay(0)
                        .build());
        dailyStay.setTotalStay(dailyStay.getTotalStay() + (int) stayedMinutes);
        dailyStayRepository.save(dailyStay);

        // 글로벌 monthly 통계 업데이트
        LocalDate monday = entryAt.toLocalDate().with(DayOfWeek.MONDAY);
        GlobalAvg globalAvg = globalAvgRepository.findByWeekStartDate(monday)
                .orElse(GlobalAvg.builder()
                        .weekStartDate(monday)
                        .globalAvg(0.0)
                        .userCnt(0)
                        .build());
        double prevAvg = globalAvg.getGlobalAvg();
        int prevCnt = globalAvg.getUserCnt();

        if(dailyStay.getTotalStay() == stayedMinutes){
            globalAvg.setGlobalAvg((prevAvg*prevCnt + stayedMinutes)/(prevCnt+1));
            globalAvg.setUserCnt(prevCnt+1);
        }
        else{
            globalAvg.setGlobalAvg((prevAvg*prevCnt + stayedMinutes)/prevCnt);
        }

        globalAvgRepository.save(globalAvg);
    }
}
