package com.deepnyangning.capstonebe.domain.face.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FaceIssueReportCreatedEvent {
    private final Long faceIssueReportId;
}
