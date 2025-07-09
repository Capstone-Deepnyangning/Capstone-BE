package com.deepnyangning.capstonebe.integration.domain.statistics.service;

import com.deepnyangning.capstonebe.domain.access.entity.AccessLog;
import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.access.event.AccessEventListener;
import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.statistics.service.CongestionService;
import com.deepnyangning.capstonebe.domain.user.entity.Role;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class CongestionServicePerformanceTest {

  // =================== 테스트 조건 변수 ===================
  private static final int TOTAL_USERS = 200; // DB에 생성할 총 사용자 수
  private static final int CURRENT_USERS = 150; // 현재 이용자로 설정할 사용자 수
  private static final int CONCURRENT_THREADS = 1000; // 동시 요청 스레드 수
  // ======================================================

  @Autowired
  private CongestionService congestionService;

  @Autowired
  private AccessLogRepository accessLogRepository;

  @Autowired
  private UserRepository userRepository;

  @MockBean
  private AccessEventListener accessEventListener;

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  private static final String CACHE_KEY_CURRENT = "congestion:current_users";
  private static final String CACHE_KEY_AVG = "congestion:avg_users";

  @BeforeEach
  void setUp() {
    redisTemplate.getConnectionFactory().getConnection().flushAll();

    List<User> users = IntStream.range(0, TOTAL_USERS)
        .mapToObj(i -> User.builder()
            .identifier("user" + i)
            .password("password123!")
            .name("테스트유저" + i)
            .role(Role.USER)
            .build())
        .collect(Collectors.toList());
    userRepository.saveAllAndFlush(users);

    List<AccessLog> pastLogs = new ArrayList<>();
    for (int i = 1; i <= 7; i++) {
      int dailyUserCount = ThreadLocalRandom.current().nextInt(100, 150);
      LocalDateTime date = LocalDateTime.now().minusDays(i);
      for (int j = 0; j < dailyUserCount; j++) {
        pastLogs.add(AccessLog.builder()
            .user(users.get(ThreadLocalRandom.current().nextInt(users.size())))
            .accessType(AccessType.ENTRY)
            .accessTime(date)
            .build());
      }
    }
    accessLogRepository.saveAllAndFlush(pastLogs);

    List<AccessLog> recentLogs = new ArrayList<>();
    for (int i = 0; i < CURRENT_USERS; i++) {
      recentLogs.add(AccessLog.builder()
          .user(users.get(i))
          .accessType(AccessType.ENTRY)
          .accessTime(LocalDateTime.now().minusHours(1))
          .build());
    }
    for (int i = CURRENT_USERS; i < TOTAL_USERS; i++) {
      recentLogs.add(AccessLog.builder()
          .user(users.get(i))
          .accessType(AccessType.ENTRY)
          .accessTime(LocalDateTime.now().minusHours(2))
          .build());
      recentLogs.add(AccessLog.builder()
          .user(users.get(i))
          .accessType(AccessType.EXIT)
          .accessTime(LocalDateTime.now().minusHours(1))
          .build());
    }
    accessLogRepository.saveAllAndFlush(recentLogs);
  }

  @Test
  @DisplayName("캐시 미적용 시, 동시 요청 성능 테스트")
  void performanceTest_Without_Cache() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    CountDownLatch latch = new CountDownLatch(CONCURRENT_THREADS);
    StopWatch stopWatch = new StopWatch();

    stopWatch.start();
    for (int i = 0; i < CONCURRENT_THREADS; i++) {
      executorService.submit(() -> {
        try {
          redisTemplate.delete(List.of(CACHE_KEY_CURRENT, CACHE_KEY_AVG));
          congestionService.getCongestionData();
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    stopWatch.stop();

    System.out.println("===== 캐시 미적용 테스트 결과 =====");
    System.out.printf("총 요청 수: %d\n", CONCURRENT_THREADS);
    System.out.printf("총 소요 시간: %d ms\n", stopWatch.getTotalTimeMillis());
  }

  @Test
  @DisplayName("캐시 적용 시, 동시 요청 성능 테스트")
  void performanceTest_With_Cache() throws InterruptedException {
    ExecutorService executorService = Executors.newFixedThreadPool(CONCURRENT_THREADS);
    CountDownLatch latch = new CountDownLatch(CONCURRENT_THREADS);
    StopWatch stopWatch = new StopWatch();

    // 첫 요청으로 캐시를 미리 생성
    congestionService.getCongestionData();

    stopWatch.start();
    for (int i = 0; i < CONCURRENT_THREADS; i++) {
      executorService.submit(() -> {
        try {
          congestionService.getCongestionData();
        } finally {
          latch.countDown();
        }
      });
    }
    latch.await();
    stopWatch.stop();

    System.out.println("\n===== 캐시 적용 테스트 결과 =====");
    System.out.printf("총 요청 수: %d\n", CONCURRENT_THREADS);
    System.out.printf("총 소요 시간: %d ms\n", stopWatch.getTotalTimeMillis());
  }
}
