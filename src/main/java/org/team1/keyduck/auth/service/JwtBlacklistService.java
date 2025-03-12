package org.team1.keyduck.auth.service;


import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.team1.keyduck.common.config.JwtUtil;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtBlacklistService {

    private final StringRedisTemplate redisTemplate;
    private final JwtUtil jwtUtil;

    public void addToBlacklist(String token) {
        redisTemplate.opsForValue().set(token, "blacklist", 6, TimeUnit.HOURS);
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }
}
