package com.deepnyangning.capstonebe.domain.access.service;

import com.deepnyangning.capstonebe.domain.access.dto.AccessResponse;
import com.deepnyangning.capstonebe.domain.access.dto.FaceAccessRequest;
import com.deepnyangning.capstonebe.domain.access.dto.QrAccessRequest;
import com.deepnyangning.capstonebe.domain.access.entity.AuthMethod;
import com.deepnyangning.capstonebe.domain.access.event.AccessCompletedEvent;
import com.deepnyangning.capstonebe.domain.notification.service.NotificationService;
import com.deepnyangning.capstonebe.domain.qr.service.QRService;
import com.deepnyangning.capstonebe.domain.statistics.service.StatisticsService;
import com.deepnyangning.capstonebe.domain.user.entity.User;
import com.deepnyangning.capstonebe.domain.user.service.UserService;
import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccessService {
    private final UserService userService;
    private final LogService logService;
    private final QRService qrService;
    private final StatisticsService statisticsService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public AccessResponse processQrAccess(QrAccessRequest request){
        String identifier;
        try{
            identifier = qrService.validateQr(request.getQrCode());
        } catch (CustomException e){
            logService.saveFailLog(AuthMethod.QR, 0.0, e.getErrorCode());
            throw e;
        }

        User user;
        try{
            user = userService.findByIdentifier(identifier);
        } catch (CustomException e){
            logService.saveFailLog(AuthMethod.QR, 0.0, e.getErrorCode());
            throw e;
        }

        logService.saveAccessLog(user, AuthMethod.QR, request.getAccessType(), 0.0);
        statisticsService.updateDailyStay(user, request.getAccessType());
        eventPublisher.publishEvent(new AccessCompletedEvent(user, request.getAccessType()));
        return AccessResponse.builder().identifier(identifier).name(user.getName()).authMethod(AuthMethod.QR).accessType(request.getAccessType()).build();
    }

    @Transactional
    public AccessResponse processFaceAccess(FaceAccessRequest request){
        String identifier = request.getIdentifier();
        User user = null;

        if(request.getSimilarity() < 0.95){
            logService.saveFailLog(AuthMethod.FACE, request.getSimilarity(), ErrorCode.INSUFFICIENT_SIMILARITY);
            throw new CustomException(ErrorCode.INSUFFICIENT_SIMILARITY);
        }

        try {
            user = userService.findByIdentifier(request.getIdentifier());
        } catch (CustomException e){
            logService.saveFailLog(AuthMethod.FACE, request.getSimilarity(), e.getErrorCode());
            throw e;
        }

        logService.saveAccessLog(user, AuthMethod.FACE, request.getAccessType(), request.getSimilarity());
        statisticsService.updateDailyStay(user, request.getAccessType());
        eventPublisher.publishEvent(new AccessCompletedEvent(user, request.getAccessType()));
        return AccessResponse.builder().identifier(identifier).name(user.getName()).authMethod(AuthMethod.FACE).accessType(request.getAccessType()).similarity(request.getSimilarity()).build();
    }
}
