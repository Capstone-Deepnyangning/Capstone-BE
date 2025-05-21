package com.deepnyangning.capstonebe.domain.statistics.service;

import com.deepnyangning.capstonebe.domain.statistics.dto.CongestionResponse;
import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CongestionService {
    private final AccessLogRepository accessLogRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String CACHE_KEY_CURRENT = "congestion:current_users";
    private static final String CACHE_KEY_AVG = "congestion:avg_users";

    // 현재 이용자 수
    public int getCurrentUsers(){
        String cached = redisTemplate.opsForValue().get(CACHE_KEY_CURRENT);
        if(cached != null){
            return Integer.parseInt(cached);
        }

        int currentUsers = accessLogRepository.countCurrentUsers();
        redisTemplate.opsForValue().set(CACHE_KEY_CURRENT, String.valueOf(currentUsers), Duration.ofMinutes(5));

        return currentUsers;
    }

    // 평균 방문자 수
    public int getAverageUsers(){
        String cached = redisTemplate.opsForValue().get(CACHE_KEY_AVG);
        if(cached != null){
            return Integer.parseInt(cached);
        }

        int avg = calculateAverage();
        redisTemplate.opsForValue().set(CACHE_KEY_AVG, String.valueOf(avg), Duration.ofHours(24));

        return avg;
    }

    // 혼잡도 데이터 반환
    public CongestionResponse getCongestionData(){
        int currentUsers = getCurrentUsers();
        int avgUsers = getAverageUsers();

        return CongestionResponse.builder()
                .currentUsers(currentUsers)
                .avgUsers(avgUsers)
                .message(generateCongestionMessage(currentUsers, avgUsers))
                .build();
    }

    // 평균 방문자 수 계산
    public int calculateAverage(){
        LocalDateTime endDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime startDate = endDate.minusDays(7);

        List<Object[]> dailyCnt = accessLogRepository.countDailyUsers(startDate, endDate);
        if(dailyCnt.isEmpty()){
            return 0;
        }

        int sum = dailyCnt.stream()
                .mapToInt(arr -> ((Long) arr[1]).intValue())
                .sum();

        return sum / dailyCnt.size();
    }

    // 혼잡도 메시지 생성
    private String generateCongestionMessage(int current, int average){
        if(average == 0){
            return "데이터 부족";
        }

        double diffPercent = ((double) (current-average)/average) * 100;
        String trend = diffPercent >= 0 ? "많음" : "적음";

        String level;
        if(diffPercent <= -20){
            level = "여유";
        }
        else if(diffPercent <= 10){
            level = "보통";
        }
        else if(diffPercent <= 30){
            level = "약간 혼잡";
        }
        else {
            level = "혼잡";
        }

        return String.format("평균보다 %.0f%% %s (%s)", diffPercent, trend, level);
    }

    // 평균 방문자 수 캐시 갱신
    @Scheduled(cron = "0 0 0 * * *")
    public void updateAverageCache(){
        int avg = calculateAverage();
        redisTemplate.opsForValue().set(CACHE_KEY_AVG, String.valueOf(avg), Duration.ofHours(24));
    }
}
