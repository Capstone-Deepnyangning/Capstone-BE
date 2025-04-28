package com.deepnyangning.capstonebe.domain.qr.service;

import com.deepnyangning.capstonebe.global.code.ErrorCode;
import com.deepnyangning.capstonebe.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Slf4j
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
        try{
            String storedIdentifier = redisTemplate.opsForValue().get(qrCode);
            if(storedIdentifier == null){
                log.warn("QR 코드가 유효하지 않습니다: {}", qrCode);
                throw new CustomException(ErrorCode.INVALID_QR_CODE);
            }
            redisTemplate.delete(qrCode);
            return storedIdentifier;
        } catch (CustomException e){
            throw e;
        } catch (Exception e) {
            log.error("Redis 작업에 실패했습니다. QR code: {}, error: {}", qrCode, e.getMessage(), e);
            throw new CustomException(ErrorCode.REDIS_OPERATION_FAILED);
        }
    }
}
