package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LogController {
    private final LogService logService;
}
