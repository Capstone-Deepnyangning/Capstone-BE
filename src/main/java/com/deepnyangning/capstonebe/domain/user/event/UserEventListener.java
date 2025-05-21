package com.deepnyangning.capstonebe.domain.user.event;

import com.deepnyangning.capstonebe.domain.face.service.FaceDataService;
import com.deepnyangning.capstonebe.external.ai.AiServerClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final FaceDataService faceDataService;
    private final AiServerClient aiServerClient;

    @TransactionalEventListener
    public void handleUserDeleted(UserDeletedEvent event){
        faceDataService.deleteFaceByUser(event.getUser());
    }

    @TransactionalEventListener
    public void handleUserDeletedForAiServer(UserDeletedEvent event){
        aiServerClient.sendDeleteFaceToAiServer(event.getUser().getIdentifier(), true);
    }
}
