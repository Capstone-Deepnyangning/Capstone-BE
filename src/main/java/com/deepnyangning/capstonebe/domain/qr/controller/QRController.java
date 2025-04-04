package com.deepnyangning.capstonebe.domain.qr.controller;

import com.deepnyangning.capstonebe.domain.qr.service.QRService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QRController {
    private final QRService qrService;
}
