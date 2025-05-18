package com.deepnyangning.capstonebe.domain.notification.service;

import com.deepnyangning.capstonebe.domain.notification.dto.FcmTokenRequest;
import com.deepnyangning.capstonebe.domain.notification.entity.FcmToken;
import com.deepnyangning.capstonebe.domain.notification.entity.PushType;
import com.deepnyangning.capstonebe.domain.notification.repository.FcmTokenRepository;
import com.deepnyangning.capstonebe.domain.notification.util.PushMessageFactory;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final FcmTokenRepository fcmTokenRepository;
    private final UserService userService;
    private final FirebaseMessaging firebaseMessaging;

    // 사용자의 FCM 토큰을 저장
    @Transactional
    public void saveToken(String identifier, String token){
        User user = userService.findByIdentifier(identifier);

        // 중복 저장 방지
        if(fcmTokenRepository.findByToken(token).isPresent()){
            return;
        }

        FcmToken fcmToken = FcmToken.builder()
                .user(user)
                .token(token)
                .build();
        fcmTokenRepository.save(fcmToken);
    }

    // 지정된 사용자에게 PushType에 따라 푸시 알림 전송
    public void sendPush(User user, PushType type){
        List<FcmToken> tokens = fcmTokenRepository.findByUser(user);

        String message = PushMessageFactory.getMessage(type);
        String targetScreen = PushMessageFactory.getTargetScreen(type);

        for(FcmToken token : tokens){
            try{
                Message fcmMessage = Message.builder()
                        .setToken(token.getToken())
                        .setNotification(Notification.builder()
                                .setTitle("딥냥닝") // 추후 변경
                                .setBody(message)
                                .build()
                        )
                        .putData("targetScreen", targetScreen)
                        .setAndroidConfig(AndroidConfig.builder()
                                .setPriority(AndroidConfig.Priority.HIGH)
                                .build())
                        .build();

                firebaseMessaging.send(fcmMessage);
            } catch (FirebaseMessagingException e) {
                log.warn("FCM 푸시 전송 실패 - token: {}, error: {}", token.getToken(), e.getMessage());
                if (e.getMessage().contains("registration-token-not-registered")) {
                    fcmTokenRepository.delete(token);
                }
            }
        }
    }

    // 관리자에게 푸시 알림 전송
    public void sendToAdmin(PushType type){
        List<User> admins = userService.findAdmins();
        for(User admin : admins){
            sendPush(admin, type);
        }
    }

    @Transactional
    public void deleteToken(String identifier, String token){
        User user = userService.findByIdentifier(identifier);
        fcmTokenRepository.deleteByUserAndToken(user, token);
    }
}
