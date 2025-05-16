package com.deepnyangning.capstonebe.domain.face.event;

import com.deepnyangning.capstonebe.domain.notification.entity.PushType;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FaceIssueReportEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFaceIssueReportCreated(FaceIssueReportCreatedEvent event){
        try{
            notificationService.sendToAdmin(PushType.FACE_ISSUE_REPORTED);
            log.info("관리자에게 안면인식 문제 신고 알림 전송 완료: reportId = {}", event.getFaceIssueReportId());
        } catch (Exception e){
            log.warn("관리자 알림 전송 실패: reportId = {}", event.getFaceIssueReportId(), e);
        }
    }
}
