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
        return CongestionResponse.builder()
                .currentUsers(getCurrentUsers())
                .avgUsers(getAverageUsers())
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

    // 평균 방문자 수 캐시 갱신
    @Scheduled(cron = "0 0 0 * * *")
    public void updateAverageCache(){
        int avg = calculateAverage();
        redisTemplate.opsForValue().set(CACHE_KEY_AVG, String.valueOf(avg), Duration.ofHours(24));
    }
}
