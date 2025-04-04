package com.deepnyangning.capstonebe.domain.access.controller;

import com.deepnyangning.capstonebe.domain.access.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccessController {
    private final AccessService accessService;
}
