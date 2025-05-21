package com.deepnyangning.capstonebe.domain.user.event;

import com.deepnyangning.capstonebe.domain.face.service.FaceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final FaceDataService faceDataService;
    private final AsyncUserEventHandler asyncUserEventHandler;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserDeleted(UserDeletedEvent event){
        faceDataService.deleteFaceByUser(event.getUser()); // 동기
        asyncUserEventHandler.sendDeleteRequestToAi(event.getUser().getIdentifier()); // 비동기
    }
}
