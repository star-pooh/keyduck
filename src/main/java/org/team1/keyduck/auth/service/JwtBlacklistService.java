package org.team1.keyduck.auth.service;


import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtBlacklistService {

    private final Cache<String, Boolean> blacklistCache;

    public JwtBlacklistService() {
        this.blacklistCache = Caffeine.newBuilder()
                .expireAfterWrite(6, TimeUnit.HOURS)
                .maximumSize(10_000)
                .build();
    }

    public void addToBlacklist(String token) {
        blacklistCache.put(token, true);
    }

    public boolean isBlacklisted(String token) {
        return blacklistCache.getIfPresent(token) != null;
    }
}
