package com.deepnyangning.capstonebe.domain.qr.service;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QRService {
    private final StringRedisTemplate redisTemplate;
    private static final long QR_EXPIRE_SECONDS = 300; // 5분

    public String generateQr(String identifier){
        // UUID로 QR 코드 생성
        String qrCode = UUID.randomUUID().toString();
        // Redis에 QR 코드 저장
        redisTemplate.opsForValue().set(qrCode, identifier, Duration.ofSeconds(QR_EXPIRE_SECONDS));
        return qrCode;
    }

    public String validateQr(String qrCode){
        String storedIdentifier = redisTemplate.opsForValue().get(qrCode);
        if(storedIdentifier == null){
            throw new CustomException(ErrorCode.INVALID_QR_CODE);
        }
        redisTemplate.delete(qrCode);
        return storedIdentifier;
    }
}
