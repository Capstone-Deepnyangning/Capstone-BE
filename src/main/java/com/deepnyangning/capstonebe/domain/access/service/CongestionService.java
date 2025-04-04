package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CongestionService {
    private final AccessLogRepository accessLogRepository;
}
