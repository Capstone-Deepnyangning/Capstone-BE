package com.deepnyangning.capstonebe.domain.face.service;

import com.deepnyangning.capstonebe.domain.face.dto.FaceIssueReportResponse;
import com.deepnyangning.capstonebe.domain.face.entity.FaceIssueReport;
import com.deepnyangning.capstonebe.domain.face.event.FaceIssueReportCreatedEvent;
import com.deepnyangning.capstonebe.domain.face.mapper.FaceIssueReportMapper;
import com.deepnyangning.capstonebe.domain.face.repository.FaceIssueReportRepository;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FaceIssueReportService {
    private final FaceIssueReportRepository faceIssueReportRepository;
    private final UserService userService;
    private final FaceIssueReportMapper faceIssueReportMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public FaceIssueReportResponse saveFaceIssueReport(String identifier){
        User user = userService.findByIdentifier(identifier);

        FaceIssueReport faceIssueReport = FaceIssueReport.builder()
                .user(user)
                .isRead(false)
                .build();

        faceIssueReportRepository.save(faceIssueReport);
        eventPublisher.publishEvent(new FaceIssueReportCreatedEvent(faceIssueReport.getId()));
        return faceIssueReportMapper.toResponseDto(faceIssueReport);
    }

    public Page<FaceIssueReportResponse> findFaceIssueReports(Pageable pageable){
        Page<FaceIssueReport> faceIssueReports = faceIssueReportRepository.findAllByOrderByCreatedAtDesc(pageable);
        return faceIssueReports.map(faceIssueReportMapper::toResponseDto);
    }

    @Transactional
    public FaceIssueReportResponse findFaceIssueReport(Long id){
        FaceIssueReport faceIssueReport = faceIssueReportRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FACE_ISSUE_REPORT_NOT_FOUND));

        faceIssueReport.setRead(true);
        return faceIssueReportMapper.toResponseDto(faceIssueReport);
    }
}
