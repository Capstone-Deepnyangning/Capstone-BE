package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.domain.face.service.FaceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FaceDataController {
    private final FaceDataService faceDataService;
}
