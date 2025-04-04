package com.deepnyangning.capstonebe.domain.notification.controller;

import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
}
