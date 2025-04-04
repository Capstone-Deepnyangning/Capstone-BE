package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.repository.AccessLogRepository;
import com.deepnyangning.capstonebe.domain.access.repository.FailLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final AccessLogRepository accessLogRepository;
    private final FailLogRepository failLogRepository;
}
