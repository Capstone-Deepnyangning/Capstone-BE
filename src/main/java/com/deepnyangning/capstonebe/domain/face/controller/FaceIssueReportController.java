package com.deepnyangning.capstonebe.domain.face.controller;

import com.deepnyangning.capstonebe.domain.face.service.FaceIssueReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FaceIssueReportController {
    private final FaceIssueReportService faceIssueReportService;
}
