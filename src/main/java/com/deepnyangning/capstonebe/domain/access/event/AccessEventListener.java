package com.deepnyangning.capstonebe.domain.access.event;

import com.deepnyangning.capstonebe.domain.access.entity.AccessType;
import com.deepnyangning.capstonebe.domain.notification.entity.PushType;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessEventListener {
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAccessCompleted(AccessCompletedEvent event){
        try{
            User user = event.getUser();
            AccessType accessType = event.getAccessType();
            notificationService.sendPush(user,
                    accessType == AccessType.ENTRY ? PushType.ENTRY_SUCCESS : PushType.EXIT_SUCCESS);
            log.info("출입 알림 전송 완료: identifier={}, accessType={}", user.getIdentifier(), accessType);
        } catch (Exception e){
            log.warn("출입 알림 전송 실패: identifier={}, accessType={}", event.getUser().getIdentifier(), event.getAccessType(), e);
        }
    }
}
