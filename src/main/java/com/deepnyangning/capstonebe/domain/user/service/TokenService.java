package com.deepnyangning.capstonebe.domain.user.service;

import com.deepnyangning.capstonebe.global.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JWTUtil jwtUtil;

    public void saveRefreshToken(String identifier, String refreshToken){
        String key = "RT:" + identifier;
        redisTemplate.opsForValue().set(key, refreshToken, jwtUtil.getRefreshExpirationTime(), TimeUnit.MILLISECONDS);
    }

    public void deleteRefreshToken(String identifier){
        redisTemplate.delete("RT:"+identifier);
    }

    public String getRefreshToken(String identifier){
        return redisTemplate.opsForValue().get("RT:"+identifier);
    }

    public void setBlacklist(String accessToken, long expiration){
        String key = "BL:" + accessToken;
        redisTemplate.opsForValue().set(key, "logout", expiration, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String accessToken){
        String key = "BL:" + accessToken;
        return redisTemplate.opsForValue().get(key) != null;
    }
}
