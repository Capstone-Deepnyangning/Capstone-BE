package com.deepnyangning.capstonebe.domain.user.event;

import com.deepnyangning.capstonebe.external.ai.AiServerClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncUserEventHandler {
    private final AiServerClient aiServerClient;

    @Async
    public void sendDeleteRequestToAi(String identifier){
        try{
            aiServerClient.sendDeleteFaceToAiServer(identifier, true);
        } catch (Exception e){
            log.error("회원 탈퇴 후 AI 서버 얼굴 데이터 삭제 요청 실패: {}", identifier, e);
        }
    }
}
