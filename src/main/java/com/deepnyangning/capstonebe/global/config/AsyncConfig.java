package com.deepnyangning.capstonebe.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);      // 최소 2개
        executor.setMaxPoolSize(4);       // 동시에 실행 가능한 최대 스레드 수
        executor.setQueueCapacity(10);    // 대기 큐 (탈퇴 요청 많지 않음)
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
