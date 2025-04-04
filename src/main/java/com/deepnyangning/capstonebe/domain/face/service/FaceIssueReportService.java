package com.deepnyangning.capstonebe.domain.face.service;

import com.deepnyangning.capstonebe.domain.face.repository.FaceIssueReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FaceIssueReportService {
    private final FaceIssueReportRepository faceIssueReportRepository;
}
